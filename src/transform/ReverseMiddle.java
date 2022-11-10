package transform;

public class ReverseMiddle implements Transformation
{
//    static String shuffle(String string)
//    {
//
//        List<Character> list = string.chars().mapToObj(c -> (char) c)
//                .collect(Collectors.toList());
//        Collections.shuffle(list);
//        StringBuilder sb = new StringBuilder();
//        list.forEach (sb::append);
//
//        return sb.toString();
//    }

    private static final String punctuations = "`~!@#$%^&*()_+{}|:\"<>?-=[]\\;\\'.\\/,'";

    private static String doForWord (String in)
    {
        if (in.length() < 4)
            return in;
        if (punctuations.contains(""+in.charAt(in.length()-1)) || punctuations.contains(""+in.charAt(0)))
        {
            String mid = in.substring(2, in.length()-2);
            StringBuilder sb = new StringBuilder (mid);
            sb.reverse();
            sb.append(in.charAt(in.length()-2)).append(in.charAt(in.length()-1));
            sb.insert (0, in.charAt(1)).insert (0, in.charAt(0));
            return sb.toString();
        }
        String mid = in.substring(1, in.length()-1);
        StringBuilder sb = new StringBuilder (mid);
        sb.reverse();
        sb.append(in.charAt(in.length()-1));
        sb.insert (0, in.charAt(0));
        return sb.toString();
    }

    private String doForText (String in)
    {
        String[] words = in.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words)
            sb.append(doForWord(word)).append(' ');
        return sb.toString();
    }

    public static void main(String[] args)
    {
        ReverseMiddle rm = new ReverseMiddle();
        String s = rm.doForText("Mit solchen Spitzenkadidaten, wie Laschet, Merz und Röttgen wird es sicher schlechter als mit Merkel und das will was bedeuten, denn diese war schon eine Zumutung für Deutschland, Europa und die Welt!");
        System.out.println(s);
    }

    @Override
    public String transform(String in)
    {
        return doForText(in);
    }

    @Override
    public String retransform(String in)
    {
        return doForText(in);
    }
}
