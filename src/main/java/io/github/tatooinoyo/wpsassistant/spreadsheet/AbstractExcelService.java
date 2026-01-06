package io.github.tatooinoyo.wpsassistant.spreadsheet;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.github.tatooinoyo.wpsassistant.spreadsheet.utils.CellWriteUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * （ 泛型：S 是 基础服务 对象，T 是实体 ）
 *
 * @author Tatooi Noyo
 * @since 1.0
 */
@RequiredArgsConstructor
public abstract class AbstractExcelService<S extends IService4Excel<T>, T, EI, EO> implements IExcelService {
    protected final S service;
    protected final IExcelConverter<T, EI, EO> excelConverter;

    abstract protected Class<EI> getExcelImportClass();

    abstract protected Class<EO> getExcelOutputClass();

    /**
     * @return 文件名, 不包含后缀
     */
    abstract protected String getFilename();

    @Override
    public void downloadTemplate(HttpServletResponse response) {
        List<EI> importElements = new ArrayList<>();
        //开始输出流
        String fileName = getFilename();
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
            EasyExcel.write(outputStream, getExcelImportClass()).sheet("sheet1").doWrite(importElements);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean importExcel(HttpServletResponse response, IMultipartFile file) {
        //文件判断
        if (null == file) {
            throw new RuntimeException("文件不能为空");
        }
        String extName = Optional.of(file).map(IMultipartFile::getOriginalFilename)
                .map(filename -> filename.split("\\."))
                .map(strArr -> {
                    if (strArr.length > 1)
                        return strArr[strArr.length - 1];
                    else
                        return "";
                })
                .orElse(null);
        if (!"xls".equals(extName) && !"xlsx".equals(extName)) {
            throw new RuntimeException("不支持的文件格式");
        }

        boolean isResult = true;


        AtomicInteger count = new AtomicInteger();


        try (InputStream forEasyExcel = file.getInputStream()) {
            EasyExcel.read(forEasyExcel, getExcelImportClass(), new PageReadListener<EI>(dataList -> {
                ArrayList<T> pos = new ArrayList<>();
                for (EI e : dataList) {

                    int num = count.getAndIncrement();
                    T po = excelConverter.toPOFromExcelElement(e);
                    pos.add(po);
                    num++;
                    count.set(num);

                }
                //插入数据库
                service.saveBatch(pos);
            }, 500)).sheet().doRead();
            if (count.intValue() == 0) {
                isResult = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return isResult;
    }

    /**
     * @param file 模拟文件对象
     * @throws RuntimeException 文件检查异常
     */
    protected void prepareCheckFile(IMultipartFile file) {
        if (null == file) {
            throw new RuntimeException("文件不能为空");
        }
        String extName = Optional.of(file).map(IMultipartFile::getOriginalFilename)
                .map(filename -> filename.split("\\."))
                .map(strArr -> {
                    if (strArr.length > 1)
                        return strArr[strArr.length - 1];
                    else
                        return "";
                })
                .orElse(null);
        if (!"xls".equals(extName) && !"xlsx".equals(extName)) {
            throw new RuntimeException("不支持的文件格式");
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response, @Nonnull List<String> ids) {
        List<EO> outputElements = service.listByIds(ids).stream()
                .map(excelConverter::toExcelFromPO)
                .toList();
        //开始输出流
        String fileName = getFilename();
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
            EasyExcel.write(outputStream, getExcelOutputClass()).registerWriteHandler(new CellWriteUtil()).sheet("sheet1").doWrite(outputElements);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 大数据导出实现，使用基于ID游标的查询和流式写入，避免内存溢出
     * 适用于导出大量数据的场景
     * 相比于传统分页，游标方式更加高效且不会出现数据重复或漏查
     *
     * @param response  HTTP响应对象
     * @param batchSize 每次查询的数据量，建议500-1000条
     */
    @Override
    public void exportLargeData(HttpServletResponse response, int batchSize) {
        if (batchSize <= 0) {
            batchSize = 500; // 默认每批次500条
        }

        String fileName = getFilename();
        ExcelWriter excelWriter = null;
        ServletOutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));

            // 创建ExcelWriter对象
            excelWriter = EasyExcel.write(outputStream, getExcelOutputClass())
                    .registerWriteHandler(new CellWriteUtil())
                    .build();

            WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();

            Serializable lastId = null; // 游标起始位null
            List<T> dataList;

            // 使用游标方式查询并写入Excel
            do {
                dataList = service.listByIdCursor(lastId, batchSize);
                if (dataList != null && !dataList.isEmpty()) {
                    // 将PO转换为Excel输出对象
                    List<EO> outputElements = dataList.stream()
                            .map(excelConverter::toExcelFromPO)
                            .toList();

                    // 写入当前批次数据
                    excelWriter.write(outputElements, writeSheet);

                    // 更新游标为当前批次最后一条记录的ID
                    T lastEntity = dataList.get(dataList.size() - 1);
                    lastId = service.getEntityId(lastEntity);
                }
            } while (dataList != null && dataList.size() == batchSize);

        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        } finally {
            // 关闭资源
            if (excelWriter != null) {
                excelWriter.finish();
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("关闭输出流失败", e);
                }
            }
        }
    }
}
