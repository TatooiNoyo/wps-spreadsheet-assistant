package io.github.tatooinoyo.wpsassistant.spreadsheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.opc.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class ImageHandler {
    private final IStorageService storageService;


    /**
     * 根据Wps中的Image函数表达式,以及映射表获取图片下载链接
     *
     * @param imageMap
     * @param imageFunExpression
     * @return
     */
    public String wpsImageFunToDownloadUrl(Map<String, String> imageMap, String imageFunExpression) {
        // 从 =DISPIMG("ID_3D02D9E9702449EB810BF35A1263A5FA",1) 中提取ID_3D02D9E9702449EB810BF35A1263A5FA
        Pattern pattern = Pattern.compile("=DISPIMG\\(\"([A-Za-z0-9_]+)\"");
        if (imageFunExpression != null) {
            Matcher matcher = pattern.matcher(imageFunExpression);

            if (matcher.find()) {
                // 找到对应的
                String id = matcher.group(1);
                return imageMap.get(id);
            }
        }
        return null;
    }

    /**
     * 读取使用WPS专用图片函数DISPIMG的Excel,提取图片然后上传,得到下载链接或文件信息json字符串对象,放入映射表中
     *
     * @param excelWithImages 内部有内嵌单元格图片的Excel
     * @return key为=DISPIMG("ID_xxx",1) 的 xxx , value为图片下载链接或文件信息json字符串对象
     */
    public Map<String, String> loadImagesFromWPSSpreadsheetAndConvertToMap(File excelWithImages) {
        Map<String, String> imageMap = new HashMap<>();
        try (InputStream forPoi = new FileInputStream(excelWithImages)) {
            imageMap = extractImagesFromWPSSpreadsheet(forPoi);
            // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        return imageMap;
    }

    public Map<String, String> extractImagesFromWPSSpreadsheet(InputStream is) {
        // imageId: ID_xxx
        HashMap<String, String> imageMap = new HashMap<>();
        try (OPCPackage pkg = OPCPackage.open(is)) {
            // 1. 拿到 cellimages.xml 及其关系
            PackagePart ciPart = pkg.getPart(PackagingURIHelper.createPartName("/xl/cellimages.xml"));
            PackageRelationshipCollection rels = new PackageRelationshipCollection(ciPart);

            // 2. DOM 解析 cellimages.xml
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document doc = dbf.newDocumentBuilder().parse(ciPart.getInputStream());

            NodeList pics = doc.getElementsByTagName("xdr:pic");
            for (int i = 0; i < pics.getLength(); i++) {
                Element pic = (Element) pics.item(i);
                rels.getRelationshipByID("rId1").getTargetURI();
                // 2.1 获取WPS 表格上的 “=DISPIMG("ID_xxx",1)” WPS 资源 ID
                // String imageId = pic.getElementsByTagName("xdr:cNvPr").item(0).getAttributes().item(1).getNodeValue();
                String imageId = pic.getElementsByTagName("xdr:cNvPr").item(0).getAttributes().getNamedItem("name").getNodeValue();
                // 2.2 获取blip rId
                String blip = ((Element) pic.getElementsByTagName("a:blip").item(0)).getAttribute("r:embed");

                // 2.3 通过 rId 找到真正的 /xl/media/xxx
                PackageRelationship rel = rels.getRelationshipByID(blip);
                PackagePart imgPart = pkg.getPart(PackagingURIHelper.createPartName(rel.getTargetURI()));
                byte[] bytes = IOUtils.toByteArray(imgPart.getInputStream());

                String extension = FilenameUtils.getExtension(imgPart.getPartName().getName());
                String filename = UUID.randomUUID() + "." + extension;
                String strContent = storageService.storage(filename, new ByteArrayInputStream(bytes));

                imageMap.put(imageId, strContent);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

        return imageMap;
    }
}

