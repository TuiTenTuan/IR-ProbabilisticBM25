package Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import DataObject.Document;

public class IRFunction 
{
    private static IRFunction instance;

    public static IRFunction Instance()
    {
        if(instance == null)
        {
            instance = new IRFunction();
        }

        return instance;
    }

    private IRFunction() { }

    public Hashtable<String, HashSet<Integer>> WorldIndex(List<Document> doc)
    {
        Hashtable<String, HashSet<Integer>> result = new Hashtable<String, HashSet<Integer>>();

        for (Document document : doc) {
            String[] worlds = document.getContent().split(" ");
        
            for (String world : worlds) 
            {
                if(result.containsKey(world))
                {
                    result.get(world).add(document.getId());                   
                }
                else
                {
                    HashSet<Integer> indexWorld = new HashSet<Integer>();
                    indexWorld.add(document.getId());

                    result.put(world, indexWorld);
                }
            }
        }
        
        return result;
    }

    public List<Document> Probabilistic (String query, List<Document> documents, Hashtable<String, HashSet<Integer>> wordIndexs, int topDocumentSuitable, int k, double b, double aveDocLength)
    {
        List<Document> result = new ArrayList<Document>();

        List<Document> tempDocuments = new ArrayList<Document>(documents);        
       
        for(int i = 0; i < tempDocuments.size(); i++)
        {
            tempDocuments.get(i).setRSV(OkapiBM25(query, i, tempDocuments, wordIndexs, k, b, aveDocLength)); //calculated RSV BM25
        }
    
        // sort by RSV
        Collections.sort(tempDocuments, Collections.reverseOrder()); 

        //get top document
        for(int i = 0; i < topDocumentSuitable; i++) 
        {
            result.add(tempDocuments.get(i));
        }

        return result;
    }

    private double OkapiBM25(String query, int indexDoc, List<Document> documents, Hashtable<String, HashSet<Integer>> wordIndexs, int k, double b, double aveDocLength)
    {
        double result = 0;

        Document document = documents.get(indexDoc);

        double lengthComponent = (1 - b) + b * (document.ContentLength() / aveDocLength);

        String[] querySplits = query.split(" ");        

        String[] docSlipts = document.getContent().split(" ");

        for (String word : querySplits) 
        {               
            int tf = 0;

            for (String wordDoc : docSlipts) 
            {
                if(word.equals(wordDoc))
                {
                    tf++;
                }                
            }

            if(tf > 0)
            {
                double tf_ = tf / lengthComponent;

                int docContainWord = wordIndexs.get(word).size();
                    
                result += Math.log10(documents.size() / docContainWord) * (((k + 1) * tf_) / (k + tf_));                     
            }    
        }

        return result;
    }
}