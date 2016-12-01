import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

public class DS {
    public static void main(String[] args) throws IOException  {

        String sourceDir = "C:\\exmp\\ex1.pdf";
        String destinationDir = "C:\\exmp\\";

        // вытаскиваем текст из отсканированного PDF файла
        try {
            System.setOut(new PrintStream(new File("C:\\exmp\\opa.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath("C:\\Temp\\tessdata");
        try {

            String result = instance.doOCR(new File(sourceDir));
            System.out.println(result);
        } catch (TesseractException e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        try {
            File sourceFile = new File(sourceDir);
            PDDocument document = PDDocument.load(sourceDir);
            List<PDPage> list = document.getDocumentCatalog().getAllPages();
            int pageNumber = 1;


            if (sourceFile.exists()) {

            // обрабатываем файл, находим  имя в тексте, передаем его новому файлу в формате png

                File f = new File("C:\\exmp\\opa.txt");
                BufferedReader fin = new BufferedReader(new FileReader(f)); // обработка выбранного файла
                String line; // обработка текста в файле
                String SS = null;
                String [] Spl; //срез
                while ((line = fin.readLine()) != null){
                    SS += line;
                }
                fin.close();

                assert SS != null;
                Spl = SS.split("[_гци]");
                for (String name : Spl) {
                    if (name.contains("Исх.№") || name.contains("Исх. №") || name.contains("Исх. № ")) {
                        System.out.println(name);
                        for (PDPage page : list) {
                            BufferedImage image = page.convertToImage();
                            File outputfile = new File(destinationDir + name + "_" + pageNumber + ".png");
                            System.out.println("Image Created -> " + outputfile.getName());
                            ImageIO.write(image, "png", outputfile);
                            pageNumber++;
                        }
                        document.close();

                    }
                }
            } else {
                System.err.println(sourceFile.getName() +" File not exists");
            }
            sourceFile.delete();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
