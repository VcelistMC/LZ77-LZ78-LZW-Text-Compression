import java.util.ArrayList;


public class LZW {
    private ArrayList<String> lzw;
    private ArrayList<LZWTag> tags;
    LZW(){
        lzw = new ArrayList<>();

        for(int i = 65; i <= 90; i++){
            lzw.add(Character.toString((char) i));

        }

        for(int i = 97; i <= 122; i++){
            lzw.add(Character.toString((char) i));
        }
        
        tags = new ArrayList<>();
    }

    public void compress(String str){
        int i = 0;
        while(i < str.length()){
            String subStr = "";
            int lastFoundIndex = 0, currFoundIndex = 0;
            while(currFoundIndex != -1 && i < str.length()){
                subStr += String.valueOf(str.charAt(i));
                currFoundIndex = lzw.indexOf(subStr);
                if(currFoundIndex !=- 1){
                    i++;
                    lastFoundIndex = currFoundIndex;
                }
            }
            lzw.add(subStr);
            if(subStr.length() == 1) lastFoundIndex = 0; // if only one letter is found, automatically adjust found index
            LZWTag newTag = new LZWTag(lastFoundIndex, subStr.charAt(subStr.length() - 1));
            tags.add(newTag);
            System.out.println(newTag);
            i++;
        }
        decompress(tags);
    }

    public void decompress(ArrayList<LZWTag> tags) {
        lzw.clear();
        lzw.add("");
        StringBuilder decodedString = new StringBuilder("");
        for(LZWTag tag : tags){
            int index = tag.index;
            char nextChar = tag.nextChar;
            lzw.add(String.valueOf(nextChar));
            decodedString.append(lzw.get(index) + nextChar);
        }
        System.out.println(decodedString.toString());
    }

    public static void main(String[] args) {
        String str = "amanda";
        LZ78 comp = new LZ78();
        comp.compress(str);
    }
}

class LZWTag {
    int index;
    char nextChar;

    LZWTag(int i, char n){
        index = i;
        nextChar = n;
    }

    @Override
    public String toString() {
        return "<"+index+", "+nextChar+">";
    }
}