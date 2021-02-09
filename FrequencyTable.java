import data_structures.CustomHashTable;

/*
 * Each URL given by the user will have an instance of this Object assigned to it.
 * 
 * This "FrequencyTable" is essentially a table of the TF-IDF values for each word,
 * hence the String being the key (representing the word), and a Double being the value
 * (representing the TF-IDF value)
 */
public class FrequencyTable extends CustomHashTable<String, Word>
{
	private int maxRawFrequency;
	private Corpus corpus;
	
	public FrequencyTable(Corpus corpus)
	{
		this.corpus = corpus;
	}
	
	public void addWord(String word)
	{
		Word wordEntry = get(word);
		
		if(wordEntry == null)
			put(word, new Word());
		else
			wordEntry.incrementRawFrequency();
	}
	
	public void calculate()
	{
		for(String key : keySet())
		{
			Word word = get(key);
			if(word != null)
				word.setTfIdf(calculateTfIdf(key));
		}
	}
	
	private double calculateTfIdf(String word)
	{
		return calculateTermFreq(word) * calculateInverseDocFreq(word);
	}
	
	private double calculateInverseDocFreq(String word)
	{
		return Math.log((double)corpus.size() / (1 + corpus.getTotalDocsContainingTerm(word)));
	}
	
	private double calculateTermFreq(String word)
	{
		Word wordEntry = get(word);
		
		int rawFreq = wordEntry == null ? 0 : wordEntry.getRawFrequency();
		
		if(rawFreq > maxRawFrequency)
			maxRawFrequency = rawFreq;
		
		return 0.5 + (0.5 * (rawFreq / maxRawFrequency));
	}
}
