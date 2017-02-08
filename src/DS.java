import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

class DS extends JFrame {

    // СЕРДЦЕ МОЕЙ ПРОГРАММЫ
    DS()  {

        //выбор обрабатываемых файлов через диалоговое окно
        JFileChooser dialog = new JFileChooser();
        dialog.setDialogTitle("Выберите файлы для сортировки");
        dialog.setMultiSelectionEnabled(true); // Разрегить выбор нескольки файлов
        dialog.showOpenDialog(this);
        File[] fileslist = dialog.getSelectedFiles();
        int filesloaded = fileslist.length; // определяем сколько файлов в обработке
        int acceptedfiles = 0; // счетчик для успешно отработанных файлов

        // выбор папки для временного хранения отработанных файлов
        JFileChooser SaveDir = new JFileChooser();
        SaveDir.setDialogTitle("Выберите Папку хранения отсортированных файлов");
        SaveDir.showOpenDialog(this);
        SaveDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        SaveDir.setAcceptAllFileFilterUsed(false);
        String put = SaveDir.getCurrentDirectory().getAbsolutePath();
        String Savefilesdir = put + "\\";

        // выбор папки для временного хранения отработанных файлов
        JFileChooser tempFilesDir = new JFileChooser();
        tempFilesDir.setDialogTitle("Выберите место временного хранилища для отработанных файлов");
        tempFilesDir.showOpenDialog(this);
        tempFilesDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        tempFilesDir.setAcceptAllFileFilterUsed(false);
        String tempput = tempFilesDir.getCurrentDirectory().getAbsolutePath();
        String tempfiledir = tempput + "\\";

        JOptionPane.showMessageDialog(null,"Пошла обработка файлов");

        // пошел перебор каждого отдельного файла из выбранных
        for(File file: fileslist){

                String filetype = file.toString();// в обработку пойдут только файлы с расширением pdf

                if(filetype.contains(".PDF")|| filetype.contains(".pdf")){


                    String sourceDir = file.toString();

                    // вытаскиваем текст из отсканированного PDF файла
                    try {
                        System.setOut(new PrintStream(new File("C:\\Temp\\opa.txt")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ITesseract instance = new Tesseract1();

                    // по этому пути расположен файл который считывает кирилицу из файла, т.е. русский текст
                    // он называется eng.traineddata
                    instance.setDatapath("C:\\Temp\\tessdata");
                    File pdffile = new File(sourceDir);
                    try {
                        ImageIO.scanForPlugins();
                        String result = instance.doOCR(pdffile);
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
                            File f = new File("C:\\Temp\\opa.txt");

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
                                        if (obj.contains("Петергофское шоссе") || obj.contains("2-я очередь")
                                                || obj.contains("ул. Тк") || obj.contains("Казакова") ) {
                                            System.out.println(obj);
                                            File filedir1 = new File(Savefilesdir + obj);
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
                                            document.close(); // завершаем работу с пдф файлом
                                        }
                                    }
                                    acceptedfiles++;//файл засчитан как успешно отсортированный

                                    // после обработки файла исходник переносится во временное хранилище
                                    sourceFile.renameTo(new File(tempfiledir, sourceFile.getName()));
                                }
                            }

                        } else {
                            System.err.println(sourceFile.getName() +" File not exists");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            setVisible(false);
            // Сообщение с отчетом о проделанной работе
        JOptionPane.showMessageDialog(null,"Загружено: " + filesloaded+ "  Успешно обработано:  " + acceptedfiles +
                                       "  Не обработано:  " +(filesloaded - acceptedfiles));
    }
}
