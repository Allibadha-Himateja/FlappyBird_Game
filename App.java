import javax.swing.JFrame;

class App
{
    public static void main(String[] args) 
    {
        int Boardheight=640;
        int Boardwidth=360;
        JFrame frame=new JFrame("Flappybird");
        Flappybird game=new Flappybird();
        game.requestFocus();
        frame.add(game);
        frame.pack();
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Boardwidth,Boardheight);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}       