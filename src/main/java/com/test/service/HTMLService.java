package com.test.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class HTMLService {


    private final FileDeletion fileDeletion;

    private final String baseFolder = "downloaded" + File.separator;


    public String upload(PartnerConnection pc, String parentId, String filepath) {
        try {
            File file = new File(filepath);
            byte[] encoded = FileUtils.readFileToByteArray(file);
            log.info("Inside upload");
            SObject sobjectNew = new SObject("ContentVersion");
            sobjectNew.setField("PathOnClient", file.getName());
            sobjectNew.setField("VersionData", encoded);
            SObject[] garage = new SObject[2];
            garage[0] = sobjectNew;
            SaveResult[] sr = pc.create(garage);
            if (sr != null && sr[0].isSuccess()) {
                String contentDocumentQuery = "SELECT ContentDocumentId FROM ContentVersion WHERE Id ='" + sr[0].getId() + "'";
                log.info(contentDocumentQuery);
                QueryResult qr = pc.query(contentDocumentQuery);
                if (qr != null && qr.isDone()) {
                    String contentDocumentId = qr.getRecords()[0].getField("ContentDocumentId").toString();
                    SObject contentDocumentLink = new SObject("ContentDocumentLink");
                    contentDocumentLink.setField("LinkedEntityId", parentId);
                    contentDocumentLink.setField("ContentDocumentId", contentDocumentId);
                    contentDocumentLink.setField("shareType", "V");
                    garage[0] = contentDocumentLink;
                    SaveResult[] cdlResult = pc.create(garage);
                    return contentDocumentId;
                }
            } else if (sr != null) {
                log.warn(sr[0].getErrors()[0].getMessage());
                return null;
            }
        } catch (ConnectionException e) {
            log.error("Unable to upload file to salesforce. {}", ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            log.error("Unknown error. {}", ExceptionUtils.getStackTrace(e));
        } finally {
            fileDeletion.deleteParentFolder(filepath);
        }
        return null;
    }

    public String convert(String html) throws IOException {
        final String randomFolder = baseFolder + UUID.randomUUID() + File.separator;
        final String outputPath = randomFolder + "output.pdf";
        FileUtils.writeStringToFile(new File(randomFolder + "input.html"), html, StandardCharsets.UTF_8);
        this.generateHtmlToPdf(randomFolder + "input.html", outputPath);
        return outputPath;
    }

    public void generateHtmlToPdf(String htmlFile, String pdfOutputFile) throws IOException {
        File inputHTML = new File(htmlFile);
        Document doc = createWellFormedHtml(inputHTML);
        xhtmlToPdf(doc, pdfOutputFile);
    }

    private Document createWellFormedHtml(File inputHTML) throws IOException {
        Document document = Jsoup.parse(inputHTML, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return document;
    }

    private void xhtmlToPdf(Document doc, String outputPdf) throws IOException {
        try (OutputStream os = new FileOutputStream(outputPdf)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(outputPdf);
            builder.toStream(os);
            builder.withW3cDocument(new W3CDom().fromJsoup(doc), Path.of(outputPdf).getParent().toString());
            builder.run();
        }
    }
}
