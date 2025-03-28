import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

class Flappybird extends JPanel implements ActionListener,MouseListener,KeyListener
{
    public static final int Boardwidth=360;
    public static final int Boardheight=640;

    int velocityX=-4;
    int velocityY=0;
    int gravity=1; 
    ArrayList<Pipe> pipes;
    Timer gameloopTimer;
    Timer pipeTimer;
    JLabel label=new JLabel();
    boolean gameover;
    double score=0.0;

    Image birdImage,backgrndImage,toppipeImage,bottompipeImage;
    //bird dimentions in bird class
    //bird class for maintaining the height width
    // for using it in the draw method
    int birdX=Boardwidth/8;
    int birdY=Boardheight/2;
    int birdheight=24;
    int birdwidth=34;
    class Bird{
        int x=birdX;
        int y=birdY;
        int height=birdheight;
        int width=birdwidth;
        Image img;
        public Bird(Image img)
        {
            this.img=img;
        }
    }
    Bird bird;

    int pipeX=Boardwidth;
    int pipeY=0;
    int pipeheight=512;
    int pipewidth=64;
    class Pipe{
        int x=pipeX;
        int y=pipeY;
        int height=pipeheight;
        int width=pipewidth;
        boolean passed;       
        Image img;
        public Pipe(Image img)
        {
            this.img=img;
        }
    }
   

    public Flappybird()
    {
        // setBackground(Color.BLUE); this for testing the filling of panel
        // backgrndImage=ImageIO.read(new File(""));
        // backgrndImage=(new ImageIcon("flappybirdbg.png")).getImage();
        backgrndImage=new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage=new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        toppipeImage=new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottompipeImage=new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird=new Bird(birdImage);
        // Game logic
        //first we have to add velocity(erraga daniki)
        //and loka loop ah loop lo game continous ga run avvali
        // ante mana panel lo changes ravali===draw method continous calling jergali
        // andhuke we use Timer in java swing
        // Timer gameloopTimer=new Timer(1000, this);
        //but naku 1 second ki 60 frames kavali
        gameloopTimer=new Timer(1000/60, this);
        // 1000 ante 1000ms anni or 1sec
        // we just have to add an actionlistener so basically ah time vachinapudu 
        // prathi 1 sec ki actionPerformed invoke avtadhi

        // ipud velocity add cheyali ante bird eggrali
        // ante bird "Y-coordinate" nunchi minus cheyali
        // action performed lo nen Y axis ni continues ga reduce cheyataniki velocity ni negative lo petta
        // if we add continuely the bird will to to top and eventually up(MOTHAM)
        // we have to create a limit

        // simlarly gravity bird anedhi kind pada daniki 
        // global variable similar to velocity but prati sari positve y ni add cheayli

        // gameloopTimer.start();
        // timer ni start cheyali ga
        pipes=new ArrayList<Pipe>();
        pipeTimer=new Timer(1500,new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                placePipes();
            }
        });

        // pipeTimer.start();
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);

        setLayout(new GridBagLayout());
        JLabel label=new JLabel("START");
        // label.setForeground(Color.green);
        label.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e)
            {
                // System.out.println("click");
                label.setText(null);
                gameloopTimer.start();
                pipeTimer.start();
            }
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
        });
        add(label,new GridBagConstraints());
    }

    public void placePipes()
    {
        /// nak intha burra led ayya
        // this random height is something different
        // this one line is soo so so so so crazy
        // we can use the math.random()*(<Range>)
        // HOW FUCKING GENIUS
        int randomY=(int)(pipeY-pipeheight/4-Math.random()*(pipeheight/2));
        int openingspace=Boardheight/4;

        Pipe toppipe=new Pipe(toppipeImage);
        toppipe.y=randomY;
        pipes.add(toppipe);

        Pipe bottomPipe=new Pipe(bottompipeImage);
        bottomPipe.y=toppipe.y+openingspace+pipeheight;
        pipes.add(bottomPipe);
    }

    public boolean collision(Bird a,Pipe b)
    {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
        a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
        a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
        a.y + a.height > b.y;
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
       // yeah its working
    //    System.out.println("draw");
        move();
        repaint();
        if(gameover)
        {
            gameloopTimer.stop();
            pipeTimer.stop();
        }
        // it will repaint anni ardham
    }

    // we cant basically use add method to add a image to the panel 
    // so we use the paintcomponent() methid and use the graphics object to use it to add the images
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // getting the graphics object to another method draw()
    public void draw(Graphics g)
    {
        g.drawImage(backgrndImage,0,0,Boardwidth, Boardheight, null);
        // Bird b=new Bird();
        label.setBounds(0,0,100,30);
        for(int i=0; i<pipes.size(); i++){
            Pipe p=pipes.get(i);
            if(collision(bird, p))
            {
                gameover=true;
            }
            g.drawImage(p.img, p.x, p.y, p.width, p.height, null);
        }
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN,32));
        if(gameover)
        {
            g.drawString("GameOver:"+String.valueOf((int)score),10,35);
            g.drawString("'SPACE'=Restart", birdX, birdY);
        }
        else
        {
            g.drawString(String.valueOf((int)score),10,35);
        }
    }

    public void move()
    {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); //apply gravity to current bird.y, limit the bird.y to top of the canvas

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                gameover = true;
            }
        }

        if (bird.y > Boardheight) {
            gameover = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)
        {
            velocityY+=-17;
            // System.out.println("pressed");
            if(gameover)
            {
                bird.y=birdY;
                velocityY=0;
                pipes.clear();
                score=0;
                gameover=false;
                gameloopTimer.start();
                pipeTimer.start();
            }
        }
    }
    public void mouseClicked(MouseEvent e)
    {
        System.out.println("click");
        velocityY+=-17;
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
}