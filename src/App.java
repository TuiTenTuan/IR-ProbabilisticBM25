//Nhóm 030
//N18DCCN200 - Võ Minh Tuấn
//N19DCCN030 - NGuyễn Thanh Dũng
//N19DCCN032 - Võ Đông Duy

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import DataObject.Document;
import Function.*;
import Variable.SystemVarible;

public class App {
    private static String pathData = "Data\\doc-text";
    private static String pathQuery = "Data\\query-text";
    private static String pathStorage = "Output\\storage-text.txt";
    private static String pathOutput = "Output\\output-text.txt";
    private static String pathStopWord = "Data\\stop-word.txt";
    private static String pathDocumentClear = "Output\\document-clear.txt";

    private static int topDocumentSuitable = 100; //1% doc
    private static int maxLoop = 1000;
    public static void main(String[] args)
    {
        //read raw data
        List<Document> rawDocuments = Data.Instance().ReadRawFileData(pathData);
        System.out.println("Documents Base Size: " + rawDocuments.size());
        
        //read stop word
        List<String> stopWords = Data.Instance().ReadStopWord(pathStopWord);
        System.out.println("Stop word Size: " + stopWords.size());

        //clear Document
        List<Document> clearDocuments = Data.Instance().RemoveStopWordDocuments(rawDocuments, stopWords);

        //storage Document clear
        if(Data.Instance().WriteDocumentClearFile(clearDocuments, pathDocumentClear))
        {
            System.out.println("Storage Document Clear Success at: " + SystemVarible.GetBaseApplicationPath() + "\\" + pathStorage);
        }
        else
        {
            System.out.println("Storage Document Clear fail!");
        }

        //create word in doc struct;
        Hashtable<String, HashSet<Integer>> worlDocIndex = IRFunction.Instance().WorldIndex(clearDocuments);
        System.out.println("Word Index Size: " + worlDocIndex.size());

        //storage word in doc
        if(Data.Instance().WriteWorldIndexFile(worlDocIndex, pathStorage))
        {
            System.out.println("Storage World Index Success at: " + SystemVarible.GetBaseApplicationPath() + "\\" + pathStorage);
        }
        else
        {
            System.out.println("Storage World Index fail!");
        }

        //read query
        List<Document> queryDocuments = Data.Instance().ReadRawFileData(pathQuery);
        System.out.println("Query Size: " + queryDocuments.size());

        //clear query
        List<Document> clearQuery = Data.Instance().RemoveStopWordDocuments(queryDocuments, stopWords, true);

        //list storage index doc
        List<HashSet<Integer>> documentResultIndexs = new ArrayList<HashSet<Integer>>();

        for (Document query : clearQuery) 
        {
            List<Document> documentResult = IRFunction.Instance().Probabilistic(query.getContent(), clearDocuments, worlDocIndex, topDocumentSuitable, maxLoop);

            HashSet<Integer> tempIndexDoc = new HashSet<Integer>();

            for (Document doc : documentResult) 
            {
                tempIndexDoc.add(doc.getId());
            }

            documentResultIndexs.add(tempIndexDoc);
        }

        //write output
        if(Data.Instance().WriteQueryOutput(documentResultIndexs, pathOutput))
        {
            System.out.println("Storage Output Success at: " + SystemVarible.GetBaseApplicationPath() + "\\" + pathOutput);
        }
        else
        {
            System.out.println("Storage Output fail!");
        }
    }    
}
