package by.tms.myonlinelibrary.service;

import by.tms.myonlinelibrary.entity.Account;
import by.tms.myonlinelibrary.entity.Book;
import by.tms.myonlinelibrary.repository.AccountRepository;
import by.tms.myonlinelibrary.repository.BookRepository;
import jakarta.persistence.EntityManager;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AccountRepository accountRepository;

    public void createBook(Book book) {
        bookRepository.save(book);
    }


    public void saveContentBook(Long bookId, MultipartFile file) {
        Book book= bookRepository.findById(bookId).get();
        try {
            if (!file.isEmpty()) {

                String contentType = file.getContentType();
                String fileName = file.getOriginalFilename();

                if (contentType != null && contentType.startsWith("text/")) {
                    book.setContent(file.getBytes()); // Сохраняем текстовый файл как есть
                } else if (fileName != null && fileName.endsWith(".pdf")) {
                    book.setContent(parsePdf(file)); // Обрабатываем PDF
                } else if (fileName != null && fileName.endsWith(".fb2")) {
                    book.setContent(parseFb2(file)); // Обрабатываем FB2
                }else {
                    throw new IllegalArgumentException("Unsupported file type");
                }
            }
            bookRepository.save(book);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

   public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).get();
   }

   public String readBook(Long bookId) {
        var book=bookRepository.findById(bookId).get();
        String content = new String(book.getContent(), StandardCharsets.UTF_8);
        return content;
   }


    public void deleteBook(Long bookId) {
        var book=bookRepository.findById(bookId).get();

        for (Account account : accountRepository.findAll()) {
            if (account.getBooks().contains(book)) {
                account.getBooks().remove(book); // Удаляем связь
                accountRepository.save(account); // Сохраняем изменения
            }
        }
        bookRepository.delete(book);
    }


    private byte[] parsePdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] parseFb2(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            NodeList bodyNodes = document.getElementsByTagName("body");
            StringBuilder content = new StringBuilder();

            for (int i = 0; i < bodyNodes.getLength(); i++) {
                Node body = bodyNodes.item(i);
                if (body.getNodeType() == Node.ELEMENT_NODE) {
                    content.append(body.getTextContent()).append("\n");
                }
            }

            return content.toString().getBytes();
        } catch (Exception e) {
            throw new IOException("Error parsing FB2 file", e);
        }
    }
}
