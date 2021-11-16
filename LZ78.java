import java.util.ArrayList;


public class LZ78 {
    private ArrayList<String> lz78;
    private ArrayList<LZWTag> tags;
    LZ78(){
        lz78 = new ArrayList<>();
        lz78.add("");
        tags = new ArrayList<>();
    }

    public void compress(String str){
        int i = 0;
        while(i < str.length()){
            String subStr = "";
            int lastFoundIndex = 0, currFoundIndex = 0;
            while(currFoundIndex != -1 && i < str.length()){
                subStr += String.valueOf(str.charAt(i));
                currFoundIndex = lz78.indexOf(subStr);
                if(currFoundIndex !=- 1){
                    i++;
                    lastFoundIndex = currFoundIndex;
                }
            }
            lz78.add(subStr);
            if(subStr.length() == 1) lastFoundIndex = 0; // if only one letter is found, automatically adjust found index
            LZWTag newTag = new LZWTag(lastFoundIndex, subStr.charAt(subStr.length() - 1));
            tags.add(newTag);
            System.out.println(newTag);
            i++;
        }
        decompress(tags);
    }

    public void decompress(ArrayList<LZWTag> tags) {
        lz78.clear();
        lz78.add("");
        StringBuilder decodedString = new StringBuilder("");
        for(LZWTag tag : tags){
            int index = tag.index;
            char nextChar = tag.nextChar;
            lz78.add(String.valueOf(nextChar));
            decodedString.append(lz78.get(index) + nextChar);
        }
        System.out.println(decodedString.toString());
    }

    public static void main(String[] args) {
        String str = "amanda";
        LZW comp = new LZW();
        comp.compress(str);
    }
}

class LZ78Tag {
    int index;
    char nextChar;

    LZ78Tag(int i, char n){
        index = i;
        nextChar = n;
    }

    @Override
    public String toString() {
        return "<"+index+", "+nextChar+">";
    }
}