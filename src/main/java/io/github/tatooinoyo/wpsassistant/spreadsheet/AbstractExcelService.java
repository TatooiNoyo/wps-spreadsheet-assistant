package io.github.tatooinoyo.wpsassistant.spreadsheet;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import io.github.tatooinoyo.wpsassistant.spreadsheet.utils.CellWriteUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
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
}
