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

    public List<Document> Probabilistic (String query, List<Document> documents, Hashtable<String, HashSet<Integer>> wordIndexs, int topDocumentSuitable, int maxLoop)
    {
        List<Document> result = new ArrayList<Document>();

        List<Document> tempDocuments = new ArrayList<Document>(documents);

        while(maxLoop > 0)
        {
            for(int i = 0; i < tempDocuments.size(); i++)
            {
                tempDocuments.get(i).setRSV(Ct(query, i, result, tempDocuments, wordIndexs)); //calculated ct
            }
        
            // sort by RSV
            Collections.sort(tempDocuments, Collections.reverseOrder()); 

            List<Document> tempResult = new ArrayList<Document>(result);

            result.clear();

            //get top document
            for(int i = 0; i < topDocumentSuitable; i++) 
            {
                result.add(tempDocuments.get(i));
            }

            maxLoop--;

            //Check converging
            for (Document document : tempResult) 
            {
                if(!result.contains(document))
                {
                    continue;
                }
            }

            //Converging
            return result;
        }

        return result;
    }

    private double Ct(String query, int indexDoc, List<Document> documentSuitables, List<Document> documents, Hashtable<String, HashSet<Integer>> wordIndexs)
    {
        double result = 0;

        String[] querySplits = query.split(" ");

        Hashtable<String, HashSet<Integer>> suitableWordIndex = WorldIndex(documentSuitables);

        String document = documents.get(indexDoc).getContent();

        for (String word : querySplits) 
        {   
            if(document.contains(word))
            {
                int docContainWord = wordIndexs.get(word) == null ? 0 : wordIndexs.get(word).size();

                if(documentSuitables.size() == 0)
                {                
                    result += Math.log10((documents.size() - docContainWord + 0.5) / (docContainWord + 0.5));
                }
                else
                {
                    int suitableDocContainWord = suitableWordIndex.get(word).size();

                    double pi = (suitableDocContainWord + 0.5) / (documentSuitables.size() + 1);
                    double ri = (docContainWord - suitableDocContainWord + 0.5) / (documents.size() - documentSuitables.size() + 1);

                    double topOfFaction = pi * (1 - ri);
                    double bottomOfFaction = ri * (1 - pi);

                    result += Math.log10(topOfFaction / bottomOfFaction);
                }
            }    
        }
        
        return result;
    }
}
