package com.ecdata.cmp.iaas.utils;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyNetworkAskVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyServiceSecurityResourcesVO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ecdata.cmp.common.utils.DateUtil.formatDate;
import static com.ecdata.cmp.common.utils.DateUtil.getNow;

/**
 * 生产PDF文件
 */
public class PDFReport{
    Document document = new Document();// 建立一个Document对象
 
    private static Font headfont ;// 设置字体大小
    private static Font keyfont;// 设置字体大小
    private static Font textfont;// 设置字体大小
 
 
    int maxWidth = 520;
    static{
        BaseFont bfChinese;
        try {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);//设置字体用宋体
            headfont = new Font(bfChinese, 12, Font.BOLD);// 标题字体大小
            keyfont = new Font(bfChinese, 9, Font.BOLD);// 关键标题字体大小
            textfont = new Font(bfChinese, 9, Font.NORMAL);// 字段字体大小
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
 
    public PDFReport(File file) {
        document.setPageSize(PageSize.A4);// 设置页面大小
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
 
    }

    public PDFReport() {

    }


    public PdfPCell createCell(String value,com.itextpdf.text.Font font,int align){
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value,font));
        return cell;
    }
    public PdfPCell createCell(String value,com.itextpdf.text.Font font){
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value,font));
        return cell;
    }
    public PdfPCell createCell(String value,com.itextpdf.text.Font font,int align,int colspan,boolean boderFlag){
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value,font));
        cell.setPadding(3.0f);
        if(!boderFlag){
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        }
        return cell;
    }
    public PdfPTable createTable(int colNumber){
        PdfPTable table = new PdfPTable(colNumber);
        try{
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        }catch(Exception e){
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 基本信息
     * @param iaasApplyResourceVO
     * @throws Exception
     */
    public void generatePDF(IaasApplyResourceVO iaasApplyResourceVO) throws Exception{

        PdfPTable table = new PdfPTable(3);
        table.addCell(createCell("基本信息", headfont,Element.ALIGN_LEFT,3,false));

        PdfPCell cell11 = new PdfPCell(new Phrase("申请单号：" + Optional.ofNullable(iaasApplyResourceVO.getProcessInstanceId()).orElse(""), textfont));
        PdfPCell cell12 = new PdfPCell(new Phrase("创建人：" + Optional.ofNullable(iaasApplyResourceVO.getCreateUserName()).orElse(""), textfont));
        PdfPCell cell13 = new PdfPCell(new Phrase("创建时间：" + formatDate(iaasApplyResourceVO.getCreateTime()), textfont));
        PdfPCell cell21 = new PdfPCell(new Phrase("所属VDC：" + Optional.ofNullable(iaasApplyResourceVO.getVdcName()).orElse(""), textfont));
        PdfPCell cell22 = new PdfPCell(new Phrase("所属project：" + Optional.ofNullable(iaasApplyResourceVO.getProjectName()).orElse(""), textfont));
        PdfPCell cell23 = new PdfPCell(new Phrase("是否项目：" + Optional.ofNullable(iaasApplyResourceVO.getIsProject()).orElse(""), textfont));
        PdfPCell cell31 = new PdfPCell(new Phrase("项目名称：" + Optional.ofNullable(iaasApplyResourceVO.getBusinessGroupName()).orElse(""), textfont));
        PdfPCell cell32 = new PdfPCell(new Phrase("紧急度：" +
                (iaasApplyResourceVO.getUrgency() != null && iaasApplyResourceVO.getUrgency().equals("0") ? "一般" : "紧急"), textfont));
        PdfPCell cell33 = new PdfPCell(new Phrase("租期：" + Optional.ofNullable(iaasApplyResourceVO.getLease()).orElse(""), textfont));
        PdfPCell cell41 = new PdfPCell(new Phrase("执行时间：" + Optional.ofNullable(iaasApplyResourceVO.getExecuteTime()).orElse(""), textfont));
        PdfPCell cell42 = new PdfPCell(new Phrase("是否现网要求：" + Optional.ofNullable(iaasApplyResourceVO.getIsCurrentNetworkEvn()).orElse(""), textfont));
        PdfPCell cell43 = new PdfPCell(new Phrase("是否需要备份：" + Optional.ofNullable(iaasApplyResourceVO.getIsBack()).orElse(""), textfont));
        PdfPCell cell51 = new PdfPCell(new Phrase("其他要求：" + Optional.ofNullable(iaasApplyResourceVO.getRemark()).orElse(""), textfont));
        PdfPCell cell52 = new PdfPCell(new Phrase("备份要求：" + Optional.ofNullable(iaasApplyResourceVO.getBackupRequest()).orElse(""), textfont));
        PdfPCell cell53 = new PdfPCell(new Phrase("附件：", textfont));
//        cell11.setBorder(Rectangle.NO_BORDER);cell21.setBorder(Rectangle.NO_BORDER);cell31.setBorder(Rectangle.NO_BORDER);
//        cellOne.setBackgroundColor(new BaseColor(255,255,45)); //设置颜色
        table.addCell(cell11);table.addCell(cell12);table.addCell(cell13);
        table.addCell(cell21);table.addCell(cell22);table.addCell(cell23);
        table.addCell(cell31);table.addCell(cell32);table.addCell(cell33);
        table.addCell(cell41);table.addCell(cell42);table.addCell(cell43);
        table.addCell(cell51);table.addCell(cell52);table.addCell(cell53);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));

        // 资源配置
        List<IaasApplyConfigInfoVO> iaasApplyConfigInfoVOList = iaasApplyResourceVO.getIaasApplyConfigInfoVOList();
        if (iaasApplyConfigInfoVOList != null && iaasApplyConfigInfoVOList.size() > 0){

            for (IaasApplyConfigInfoVO iaasApplyConfigInfoVO : iaasApplyConfigInfoVOList) {
                // 1.虚拟机，2.裸金属，3.安全资源
                if (iaasApplyConfigInfoVO.getApplyType() != null && iaasApplyConfigInfoVO.getApplyType().equals("3")) {
                    table.addCell(createCell("资源配置", headfont,Element.ALIGN_LEFT,3,false));
                    PdfPCell cell1 = new PdfPCell(new Phrase("服务器类型：安全资源" , textfont));
                    // 1.新增；2.变更;3.删除
                    String operationType = "";
                    if (iaasApplyConfigInfoVO.getOperationType() != null) {
                        switch (iaasApplyConfigInfoVO.getOperationType()) {
                            case "1":
                                operationType = "新增";
                            case "2":
                                operationType = "变更";
                            case "3":
                                operationType = "删除";
                        }
                    }
                    PdfPCell cell2 = new PdfPCell(new Phrase("操作类型：" + operationType, textfont));
                    String ccrc = "";
                    if (iaasApplyConfigInfoVO.getIsTwoCcrc() != null && iaasApplyConfigInfoVO.getIsTwoCcrc().equals("1")) {
                        ccrc = "二级等保";
                    } else if (iaasApplyConfigInfoVO.getIsThreeCcrc() != null && iaasApplyConfigInfoVO.getIsThreeCcrc().equals("1")){
                        ccrc = "三级等保";
                    }
                    PdfPCell cell3 = new PdfPCell(new Phrase("等保标准：" + ccrc, textfont));
                    String nextNfw = "";
                    int nextNfwNum = 0;
                    if (iaasApplyConfigInfoVO.getIaasApplyServiceSecurityResourcesVO() != null){
                        nextNfw = iaasApplyConfigInfoVO.getIaasApplyServiceSecurityResourcesVO().getNextNfw();
                        nextNfwNum = iaasApplyConfigInfoVO.getIaasApplyServiceSecurityResourcesVO().getNextNfwNum();
                    }
                    PdfPCell cell4 = new PdfPCell(new Phrase("下一代防火墙：" + nextNfw, textfont));
                    PdfPCell cell5 = new PdfPCell(new Phrase("数量：" + nextNfwNum, textfont));
                    PdfPCell cell6 = new PdfPCell(new Phrase("其他要求：" + Optional.ofNullable(iaasApplyConfigInfoVO.getOtherRequire()).orElse(""), textfont));
                    table.addCell(cell1);table.addCell(cell2);table.addCell(cell3);table.addCell(cell4);table.addCell(cell5);table.addCell(cell6);
                } else {
                    table.addCell(createCell("资源配置", headfont,Element.ALIGN_LEFT,3,false));
                    String applyType = "";
                    if (iaasApplyConfigInfoVO.getApplyType() != null) {
                        switch (iaasApplyConfigInfoVO.getApplyType()) {
                            case "1":
                                applyType = "虚拟机";
                            case "2":
                                applyType = "裸金属";
                            case "3":
                                applyType = "安全资源";
                        }
                    }
                    PdfPCell cell111 = new PdfPCell(new Phrase("服务器类型：" + applyType, textfont));
                    // 1.新增；2.变更;3.删除
                    String operationType = "";
                    if (iaasApplyConfigInfoVO.getOperationType() != null) {
                        switch (iaasApplyConfigInfoVO.getOperationType()) {
                            case "1":
                                operationType = "新增";
                            case "2":
                                operationType = "变更";
                            case "3":
                                operationType = "删除";
                        }
                    }
                    PdfPCell cell112 = new PdfPCell(new Phrase("操作类型：" + operationType, textfont));
                    PdfPCell cell113 = new PdfPCell(new Phrase("服务器名称：" + Optional.ofNullable(iaasApplyConfigInfoVO.getServerName()).orElse(""), textfont));
                    PdfPCell cell121 = new PdfPCell(new Phrase("操作系统：" + Optional.ofNullable(iaasApplyConfigInfoVO.getOperationSystem()).orElse(""), textfont));
                    PdfPCell cell122 = new PdfPCell(new Phrase("CPU：" + Optional.ofNullable(iaasApplyConfigInfoVO.getCpu()).orElse(0), textfont));
                    PdfPCell cell123 = new PdfPCell(new Phrase("内存：" + Optional.ofNullable(iaasApplyConfigInfoVO.getMemory()).orElse(0), textfont));
                    PdfPCell cell131 = new PdfPCell(new Phrase("对应华为云EIP地址：" + Optional.ofNullable(iaasApplyConfigInfoVO.getEip()).orElse(""), textfont));
                    PdfPCell cell132 = new PdfPCell(new Phrase("密码：" + Optional.ofNullable(iaasApplyConfigInfoVO.getPassword()).orElse(""), textfont));
                    PdfPCell cell133 = new PdfPCell(new Phrase("系统盘（G）：" + Optional.ofNullable(iaasApplyConfigInfoVO.getSystemDisk()).orElse(0D), textfont));
                    PdfPCell cell141 = new PdfPCell(new Phrase("数量：" + Optional.ofNullable(iaasApplyConfigInfoVO.getVmNum()).orElse(0), textfont));
                    PdfPCell cell142 = new PdfPCell(new Phrase("是否需要互联网访问：" + Optional.ofNullable(iaasApplyConfigInfoVO.getPortMapping()).orElse(""), textfont));
                    PdfPCell cell143 = new PdfPCell(new Phrase("端口映射要求：" + Optional.ofNullable(iaasApplyConfigInfoVO.getNativePort()).orElse(""), textfont));
                    PdfPCell cell151 = new PdfPCell(new Phrase("是否灾备服务器：" + Optional.ofNullable(iaasApplyConfigInfoVO.getIsDisasterServer()).orElse(""), textfont));
                    String ccrc = "";
                    if (iaasApplyConfigInfoVO.getIsTwoCcrc() != null && iaasApplyConfigInfoVO.getIsTwoCcrc().equals("1")) {
                        ccrc = "二级等保";
                    } else if (iaasApplyConfigInfoVO.getIsThreeCcrc() != null && iaasApplyConfigInfoVO.getIsThreeCcrc().equals("1")){
                        ccrc = "三级等保";
                    }
                    PdfPCell cell152 = new PdfPCell(new Phrase("等保标准：" + ccrc, textfont));
                    String isPre = "";
                    if (iaasApplyConfigInfoVO.getIaasApplyServiceSecurityResourcesVO() != null && iaasApplyConfigInfoVO.getIaasApplyServiceSecurityResourcesVO().getIsPreventChange() != null) {
                        isPre = iaasApplyConfigInfoVO.getIaasApplyServiceSecurityResourcesVO().getIsPreventChange();
                    }
                    PdfPCell cell153 = new PdfPCell(new Phrase("防篡改 ：" + isPre, textfont));

                    table.addCell(cell111);
                    table.addCell(cell112);
                    table.addCell(cell113);
                    table.addCell(cell121);
                    table.addCell(cell122);
                    table.addCell(cell123);
                    table.addCell(cell131);
                    table.addCell(cell132);
                    table.addCell(cell133);
                    table.addCell(cell141);
                    table.addCell(cell142);
                    table.addCell(cell143);
                    table.addCell(cell151);
                    table.addCell(cell152);
                    table.addCell(cell153);
//                 cellOne.setBackgroundColor(new BaseColor(255,255,45)); //设置颜色

                    List<IaasApplyNetworkAskVO> iaasApplyNetworkAskVO = iaasApplyConfigInfoVO.getIaasApplyNetworkAskVO();
                    if (iaasApplyNetworkAskVO != null && iaasApplyNetworkAskVO.size() > 0) {
                        table.addCell(createCell("网络要求：", textfont, Element.ALIGN_LEFT, 3, false));
                        for (IaasApplyNetworkAskVO applyNetworkAskVO : iaasApplyNetworkAskVO) {
                            PdfPCell cellNet1 = new PdfPCell(new Phrase("本机端口：" + Optional.ofNullable(applyNetworkAskVO.getNativePort()).orElse(""), textfont));
                            PdfPCell cellNet2 = new PdfPCell(new Phrase("对方服务器：" + Optional.ofNullable(applyNetworkAskVO.getOppositeServer()).orElse(""), textfont));
                            PdfPCell cellNet3 = new PdfPCell(new Phrase("对方端口：" + Optional.ofNullable(applyNetworkAskVO.getOppositePort()).orElse(""), textfont));
                            PdfPCell cellNet4 = new PdfPCell(new Phrase("访问方式：" + Optional.ofNullable(applyNetworkAskVO.getAccessMode()).orElse(""), textfont));
                            PdfPCell cellNet5 = new PdfPCell(new Phrase("要求：" + Optional.ofNullable(applyNetworkAskVO.getRequires()).orElse(""), textfont));
                            PdfPCell cellNet6 = new PdfPCell(new Phrase("", textfont));
                            table.addCell(cellNet1);
                            table.addCell(cellNet2);
                            table.addCell(cellNet3);
                            table.addCell(cellNet4);
                            table.addCell(cellNet5);
                            table.addCell(cellNet6);
                        }
                    }
                    table.addCell(createCell("软件要求：" +
                            Optional.ofNullable(iaasApplyConfigInfoVO.getSoftwareRequire()).orElse(""), textfont, Element.ALIGN_LEFT, 3, false));
                    table.addCell(createCell("其他要求：" +
                            Optional.ofNullable(iaasApplyConfigInfoVO.getOtherRequire()).orElse(""), textfont, Element.ALIGN_LEFT, 3, false));

                }
            }
        }

        document.add(table);

        document.close();
    }

    public static void main(String[] args) throws Exception {

        System.out.println("------start--------");
        File file = new File("D:\\test\\text1.pdf");
        file.createNewFile();//在指定目录下创建一个文件

        IaasApplyResourceVO iaasApplyResourceVO = new IaasApplyResourceVO();
        iaasApplyResourceVO.setProcessInstanceId("746287648732");
        iaasApplyResourceVO.setCreateUserName("san.zhang");
        iaasApplyResourceVO.setCreateTime(getNow());
        iaasApplyResourceVO.setVdcName("vdc_SW");
        iaasApplyResourceVO.setIsProject("是");
        iaasApplyResourceVO.setProjectName("水务集团");

        IaasApplyConfigInfoVO iaasApplyConfigInfoVO = new IaasApplyConfigInfoVO();
        iaasApplyConfigInfoVO.setId(99837871166853127L);
        List<IaasApplyNetworkAskVO> list2 = new ArrayList<>();
        IaasApplyNetworkAskVO iaasApplyNetworkAskVO = new IaasApplyNetworkAskVO();
        iaasApplyNetworkAskVO.setId(99837871166853127L);
        list2.add(iaasApplyNetworkAskVO);
        iaasApplyConfigInfoVO.setIaasApplyNetworkAskVO(list2);
        IaasApplyServiceSecurityResourcesVO iaasApplyServiceSecurityResourcesVO = new IaasApplyServiceSecurityResourcesVO();
        iaasApplyServiceSecurityResourcesVO.setId(99837871166853127L);
        iaasApplyConfigInfoVO.setIaasApplyServiceSecurityResourcesVO(iaasApplyServiceSecurityResourcesVO);
        List<IaasApplyConfigInfoVO> list = new ArrayList<>();
        list.add(iaasApplyConfigInfoVO);
        IaasApplyConfigInfoVO iaasApplyConfigInfoVO11 = new IaasApplyConfigInfoVO();
        iaasApplyConfigInfoVO11.setApplyType("3");
        list.add(iaasApplyConfigInfoVO11);
        iaasApplyResourceVO.setIaasApplyConfigInfoVOList(list);

        new PDFReport(file).generatePDF(iaasApplyResourceVO);

    }
    
}