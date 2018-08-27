package com.ftn.service.impl;

import com.ftn.exception.BadRequestException;
import com.ftn.exception.IncompleteIndexDocumentException;
import com.ftn.exception.NotFoundException;
import com.ftn.exception.ParseQueryException;
import com.ftn.lucene.EBookIndexer;
import com.ftn.lucene.handler.DocumentHandler;
import com.ftn.lucene.handler.PDFHandler;
import com.ftn.lucene.model.ResultData;
import com.ftn.lucene.query.QueryBuilder;
import com.ftn.lucene.query.QueryOperator;
import com.ftn.lucene.query.QueryType;
import com.ftn.lucene.retriever.InformationRetriever;
import com.ftn.model.EBook;
import com.ftn.model.dto.EBookBaseDTO;
import com.ftn.model.dto.EBookDTO;
import com.ftn.model.dto.MultipleFieldSearchDTO;
import com.ftn.model.dto.SingleFieldSearchDTO;
import com.ftn.repository.EBookDao;
import com.ftn.service.EBookService;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.lang.model.element.ElementVisitor;
import javax.xml.transform.Result;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Service
public class EBookServiceImpl implements EBookService {

    @Autowired
    private EBookDao eBookDao;

    private static final String DATA_PATH = "src\\main\\webapp\\eBooks\\";

    private static final String INDEX_PATH = "src\\main\\resources\\indexes\\";

    private DocumentHandler documentHandler;

    private EBookIndexer eBookIndexer;

    @PostConstruct
    public void init() {
        documentHandler = new PDFHandler();
        eBookIndexer = new EBookIndexer(INDEX_PATH);
    }

    @Override
    public List<EBookDTO> readAll() {
        return eBookDao.findAll().stream().map(eBook -> new EBookDTO(eBook)).collect(Collectors.toList());
    }

    @Override
    public List<EBookDTO> readByCategory(Long categoryId) {
        return eBookDao.findByCategoryId(categoryId).stream().map(eBook -> new EBookDTO(eBook)).collect(Collectors.toList());
    }

    @Override
    public EBookDTO readById(Long id) {
        EBook eBook = eBookDao.findById(id).orElseThrow(NotFoundException::new);
        return new EBookDTO(eBook);
    }

    @Override
    public EBookDTO create(EBookDTO eBookDTO) {
        EBook eBook = new EBook();
        eBook.merge(eBookDTO);
        eBook = eBookDao.save(eBook);
        return new EBookDTO(eBook);
    }

    @Override
    public EBookDTO saveAndIndex(EBookDTO eBookDTO) {
        boolean success;
        EBookDTO retEBookDTO = null;
        String fileName = DATA_PATH + eBookDTO.getFilename();
        File file = new File(fileName);

        if (file.exists() && !file.isDirectory()) {
            EBook eBook = new EBook();
            eBook.merge(eBookDTO);
            eBook = eBookDao.save(eBook);
            success = indexEBook(eBook);
            if (success) {
                retEBookDTO = new EBookDTO(eBook);
                retEBookDTO.getCataloguer().setPassword(null);
            }
        }
        return retEBookDTO;
    }

    @Override
    public EBookDTO update(Long id, EBookDTO eBookDTO) {
        EBookDTO retEBookDTO = null;
        EBook eBook = eBookDao.findById(id).orElseThrow(NotFoundException::new);
        eBook.merge(eBookDTO);
        eBook = eBookDao.save(eBook);
        if (indexEBook(eBook)) {
            retEBookDTO = new EBookDTO(eBook);
        }
        return retEBookDTO;
    }

    @Override
    public boolean delete(Long id) {
        boolean success = false;
        final EBook eBook = eBookDao.findById(id).orElseThrow(NotFoundException::new);
        if (deleteEBookIndex(eBook) && deleteFile(eBook.getFilename())) {
            eBookDao.delete(eBook);
            success = true;
        }
        return success;
    }

    @Override
    public File saveEBookFile(byte[] bytes) throws IOException {
        File eBookFile = createEBookFile();
        FileOutputStream fileOutputStream = new FileOutputStream(eBookFile);
        fileOutputStream.write(bytes);
        fileOutputStream.close();
        return eBookFile;
    }

    @Override
    public EBookBaseDTO prepareBase(File file) {
        EBookBaseDTO eBookBaseDTO = null;
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            PDDocumentInformation documentInformation = document.getDocumentInformation();
            String bla = file.getName();
            eBookBaseDTO = new EBookBaseDTO();
            eBookBaseDTO.setTitle(documentInformation.getTitle());
            eBookBaseDTO.setAuthor(documentInformation.getAuthor());
            eBookBaseDTO.setKeywords(documentInformation.getKeywords());
            eBookBaseDTO.setFilename(file.getName());
            document.close();

            return eBookBaseDTO;

        } catch (IOException e) {
            return eBookBaseDTO;
        }
    }

    @Override
    public boolean indexEBook(EBook eBook) {
        boolean success;
        File file = getFile(eBook);
        Document document = null;
        try {
            document = documentHandler.getDocument(eBook, file);
            if (eBook.getId() != null) {
                eBookIndexer.deleteDocumentByIdField(eBook.getId().toString());
            }
            eBookIndexer.addDocumentToIndex(document);
            success = true;
        } catch (IncompleteIndexDocumentException e) {
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteEBookIndex(EBook eBook) {
        boolean success;
        File file = getFile(eBook);
        Document document;
        try {
            document = documentHandler.getDocument(eBook, file);
            eBookIndexer.deleteDocument(document);
            success = true;
        } catch (IncompleteIndexDocumentException e) {
            success = false;
        }
        return success;
    }

    @Override
    public File getFile(EBook eBook) {
        File file;
        String fullFilePath = DATA_PATH + eBook.getFilename();
        file = new File(fullFilePath);
        return file;
    }

    @Override
    public File getFile(Long id) {
        EBook eBook = eBookDao.findById(id).orElseThrow(NotFoundException::new);
        return getFile(eBook);
    }

    @Override
    public boolean deleteFile(String filename) {
        boolean success;
        String pathName = DATA_PATH + filename;
        try {
            File file = new File(pathName);
            if (file.delete()) {
                success = true;
            } else {
                success = false;
            }
        } catch (Exception e) {
            success = false;
        }

        return success;
    }

    @Override
    public Resource loadFile(Long id) {
        Resource loaded;
        EBook eBook = eBookDao.findById(id).orElseThrow(NotFoundException::new);
        File file = getFile(eBook);
        Path filePath = Paths.get(file.getAbsolutePath());
        try {
            loaded = new ByteArrayResource(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new BadRequestException("Requested wrong file for download!");
        }
        return loaded;
    }

    @Override
    public String loadFileString(Long id) {
        String encoded = "";
        File file = getFile(id);
        if (file != null) {
            try {
                Path path = file.toPath();
                byte[] dataPDF = Files.readAllBytes(path);
                byte[] encodedBytes = Base64.getEncoder().encode(dataPDF);
                encoded = new String(encodedBytes);
            } catch (IOException e) {
                encoded = "";
            }
        }
        return encoded;
    }

    @Override
    public List<ResultData> search(SingleFieldSearchDTO singleFieldSearchDTO) {
        String fieldName = singleFieldSearchDTO.getFieldName().trim();
        String fieldValue = singleFieldSearchDTO.getFieldValue().trim();
        QueryType queryType = QueryType.valueOf(singleFieldSearchDTO.getQueryType());
        List<ResultData> resultData;

        try {
            Query query = QueryBuilder.buildQuery(queryType, fieldName, fieldValue);
            InformationRetriever informationRetriever = new InformationRetriever(DATA_PATH, INDEX_PATH);
            List<String> queriedHighlights = new ArrayList<>();

            queriedHighlights.add(fieldName.trim());
            resultData = informationRetriever.retrieveEBooks(query, queriedHighlights, Sort.INDEXORDER);
        } catch (ParseQueryException e) {
            resultData = null;
        }
        return resultData;
    }

    @Override
    public List<ResultData> search(MultipleFieldSearchDTO multipleFieldSearchDTO) {
        List<ResultData> resultData;
        InformationRetriever informationRetriever = new InformationRetriever(DATA_PATH, INDEX_PATH);
        List<String> requiredHighlights = new ArrayList<>();
        QueryType queryType = QueryType.valueOf(multipleFieldSearchDTO.getQueryType());
        QueryOperator queryOperator = QueryOperator.valueOf(multipleFieldSearchDTO.getQueryOperator());

        BooleanClause.Occur occur = queryOperator.equals(QueryOperator.AND) ?
                BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD;
        BooleanQuery booleanQuery = new BooleanQuery();

        try {
            if (multipleFieldSearchDTO.getTitle() != null && !multipleFieldSearchDTO.getTitle().trim().isEmpty()) {
                requiredHighlights.add("title");
                booleanQuery.add(QueryBuilder.buildQuery(queryType, "title", multipleFieldSearchDTO.getTitle().trim()), occur);
            }
            if (multipleFieldSearchDTO.getAuthor() != null && !multipleFieldSearchDTO.getAuthor().trim().isEmpty()) {
                requiredHighlights.add("author");
                booleanQuery.add(QueryBuilder.buildQuery(queryType, "author", multipleFieldSearchDTO.getAuthor().trim()), occur);
            }
            if (multipleFieldSearchDTO.getKeywords() != null && !multipleFieldSearchDTO.getKeywords().trim().isEmpty()) {
                requiredHighlights.add("keyword");
                List<Query> queries = buildQueriesForKeywords(queryType, occur, multipleFieldSearchDTO);
                queries.forEach(query -> booleanQuery.add(query, occur));
            }
            if (multipleFieldSearchDTO.getContent() != null && !multipleFieldSearchDTO.getContent().trim().isEmpty()) {
                requiredHighlights.add("content");
                booleanQuery.add(QueryBuilder.buildQuery(queryType, "content", multipleFieldSearchDTO.getContent().trim()), occur);
            }
            if (multipleFieldSearchDTO.getLanguage() != null && !multipleFieldSearchDTO.getLanguage().trim().isEmpty()) {
                requiredHighlights.add("language");
                booleanQuery.add(QueryBuilder.buildQuery(queryType, "language", multipleFieldSearchDTO.getLanguage().trim()), occur);
            }

            resultData = informationRetriever.retrieveEBooks(booleanQuery, requiredHighlights, Sort.INDEXORDER);
        } catch (ParseQueryException e) {
            resultData = null;
        }
        return resultData;
    }

    @Override
    public String getFileName(Long id) {
        String filename = "";
        EBook eBook = eBookDao.findById(id).orElseThrow(NotFoundException::new);
        filename = eBook.getFilename();
        return filename;
    }

    private File createEBookFile() throws IOException {
        File repository = new File(DATA_PATH);
        File eBookFile = File.createTempFile("ebook_", ".pdf", repository);
        return eBookFile;
    }

    private List<Query> buildQueriesForKeywords(QueryType queryType, BooleanClause.Occur occur,
                                                        MultipleFieldSearchDTO multipleFieldSearchDTO){
        List<Query> queries = new ArrayList<>();
        String keywords = multipleFieldSearchDTO.getKeywords();
        if(keywords.contains(",")){
            String [] parts = keywords.split(",");
            for (String part :parts) {
                if(!part.isEmpty()) {
                    try {
                        Query query = QueryBuilder.buildQuery(queryType, "keyword", part.trim());
                        queries.add(query);
                    } catch (ParseQueryException e) {
                        throw new BadRequestException("Keywords error.");
                    }
                }
            }
        }else {
            try {
                Query query = QueryBuilder.buildQuery(queryType, "keyword", keywords.trim());
                queries.add(query);
            } catch (ParseQueryException e) {
                throw new BadRequestException("Keywords error.");
            }
        }
        return queries;
    }
}
