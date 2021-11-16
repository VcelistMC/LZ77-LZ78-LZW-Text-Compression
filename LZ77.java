import java.util.ArrayList;
import java.util.Arrays;

public class LZ77 {
    ArrayList<LZ77Tag> lz77;
    final int SEARCH_WINDOWS_SIZE = 14;
    final int BUFFER_WINDOW_SIZE = 10;
    
    public LZ77(){
        lz77 = new ArrayList<>();
    }
    
    ArrayList<LZ77Tag> getLastTagList(){ return lz77; }

    public ArrayList<LZ77Tag> compress(String text) {
        lz77.clear();
        int i = 0;
        while(i < text.length()){
            int[] bestVals = findMAtch(i, text);
            int distance = bestVals[0], length = bestVals[1];
            char nextChar;

            if(i+length >= text.length())
                nextChar = '\u0000';
            else
                nextChar = text.charAt(i+length);
            
            // aaabaaac
            
            LZ77Tag newTag = new LZ77Tag(distance, length, nextChar); 
            lz77.add(newTag);
            System.out.println(newTag.toString());
            i += (length == 0)? 1 : length+1;
        }
        
        return getLastTagList();
    }

    private int[] findMAtch(int encodedStart, String text) {
        int bestDistance = 0, currDistance = 0;
        int bestLength = 0, currLength = 0;
        int currPos = encodedStart-1;
        int currSearchWindowSize = max(0, currPos - SEARCH_WINDOWS_SIZE);
        int currBufferWindowSize = min(encodedStart + BUFFER_WINDOW_SIZE, text.length());

            // aaabaaac

        while(currPos >= currSearchWindowSize) {
            if(text.charAt(encodedStart) == text.charAt(currPos)){
                int tempEncodingIndex = currPos+1; //b
                int tempEncodedIndex = encodedStart+1; //a
                currDistance = encodedStart - currPos; //2
                currLength = 1; 
                if(tempEncodedIndex < text.length()) {
                    while(text.charAt(tempEncodingIndex) == text.charAt(tempEncodedIndex)){
                        currLength++;
                        tempEncodedIndex++;
                        tempEncodingIndex++;
                        if(tempEncodedIndex == currBufferWindowSize)
                            break;
                    }
                }
                if(currLength > bestLength){
                    bestLength = currLength;
                    bestDistance = currDistance;
                }
            }
            currPos--;
        }
        
        int[] arr = {bestDistance, bestLength};
        return arr; 
    }

    public String decompress(ArrayList<LZ77Tag> tags){
        StringBuilder decodedString = new StringBuilder("");
        // int currPos = 0;
        for(LZ77Tag tag : tags){
            int SegmantStartIndex = decodedString.length() - tag.start;
            if(tag.start == 0){
                decodedString.append(tag.nextChar);
                continue;
            }
            for(int currSegmantPos = 0; currSegmantPos < tag.length; currSegmantPos++){
                decodedString.append(decodedString.charAt(SegmantStartIndex+currSegmantPos));
            }
            // currPos +
            decodedString.append(tag.nextChar);
        }
        return decodedString.toString();
    }
 
    int max(int a, int b){ return a>b? a:b; }

    int min(int a, int b){ return a>b? b:a; }

    public static void main(String[] args) {
        LZ77 comp = new LZ77();
        String str = "does it work";
        System.out.println(str.length());
        ArrayList<LZ77Tag> tags = new ArrayList<LZ77Tag>(
            Arrays.asList(
                new LZ77Tag(0,0,'A'),
                new LZ77Tag(0,0,'B'),
                new LZ77Tag(2,1,'A'),
                new LZ77Tag(3,2,'B'),
                new LZ77Tag(5, 3,'B'),
                new LZ77Tag(1, 10,'A')
            )
        );
        comp.compress(str);
    
    }
}

class LZ77Tag {
    int start;
    int length;
    char nextChar;

    public LZ77Tag(int s, int l, char n){
        start = s;
        length = l;
        nextChar = n;
    }
    @Override
    public String toString() {
        return "<"+start+","+length+","+nextChar+">";
    }
}