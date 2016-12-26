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

        // вытаскиваем текст из отсканированного PDF файла
        try {
            System.setOut(new PrintStream(new File("C:\\exmp\\opa.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ITesseract instance = new Tesseract1();

        // по этому пути расположен файл который считывает кирилицу из файла, т.е. русский текст
        // он называется eng.traineddata
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

                // обработка выбранного файла
                BufferedReader fin = new BufferedReader(new FileReader(f));

                // считывание текста построчно
                String line;

                // счетчик считывемого текста по символам
                String SS = null;

                //срез для имени файла
                String [] Spl;

                //срез для директории названия объекта
                String [] Sp2;

                while ((line = fin.readLine()) != null){
                    SS += line;
                }
                fin.close();

                assert SS != null;
                Spl = SS.split("[_гци]");
                Sp2 = SS.split("[«»()]");

                // вытаскиваем текст с именем файла
                for (String name : Spl) {
                    if (name.contains("Исх.№") || name.contains("Исх. №") || name.contains("Исх. № ")) {
                        System.out.println(name);

                        // вытаскиванием текст для названия дериктории с именем объекта строительства
                        for (String obj : Sp2) {
                            if (obj.contains("Петергофское шоссе") || obj.contains("2-я очередь") ) {
                                System.out.println(obj);
                                File filedir1 = new File("C:\\" + obj);
                                if (!filedir1.exists()) {

                                    // создаем новую папку с названием объекта строительства
                                    filedir1.mkdir();
                                    System.out.println("Directory is created!");
                                }

                                // обрабатываем пдф файл, присваиваем ему имя, задаем путь
                                for (PDPage page : list) {
                                    BufferedImage image = page.convertToImage();
                                    File outputfile = new File(filedir1+"\\"+ name + "_" + pageNumber + ".png");
                                    System.out.println("Image Created -> " + outputfile.getName());
                                    ImageIO.write(image, "png", outputfile);
                                    pageNumber++;
                                }
                                // завершаем работу с пдф файлом
                                document.close();
                            }
                        }
                    }
                }
            } else {
                System.err.println(sourceFile.getName() +" File not exists");
            }

            // после обработки файла исходник удаляется
            sourceFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
