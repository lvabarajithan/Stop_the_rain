package com.abara.stoptherain;

import java.util.Random;

/**
 * Created by Abb on 8/27/2015.
 */
public class Fun {

    private String[] funMsgRaining = {"Hey, It's raining. Why don't you try stopping it?",
            "Seems to rain, come on stop it.",
            "Heading out anywhere? It's gonna rain. Stop it buddy.",
            "Rain detected. Come on lets stop it.",
            "Hit the button before the drop hits you."
    };

    private String[] funMsgNotRaining = {"It's not raining, how could you stop it?",
            "No rain detected. Need an app to call rain?",
            "Seems no rain, better luck next time.",
            "Planned to go out anywhere? Do it before it rains.",
            "Wait till it rains, so that you can stop it."
    };

    private int[] funImgRaining = {R.drawable.ic_happy_1,
            R.drawable.ic_happy_2,
            R.drawable.ic_happy_3,
            R.drawable.ic_happy_4,
            R.drawable.ic_happy_5
    };

    private int[] funImgNotRaining = {R.drawable.ic_sad_1,
            R.drawable.ic_sad_2,
            R.drawable.ic_sad_3,
            R.drawable.ic_sad_4,
            R.drawable.ic_sad_5
    };
    private Random rand;

    private int index=0;

    public Fun(){
        rand = new Random();
    }

    public String getFunText(boolean bool){
        if(bool){
            return funMsgRaining[rand.nextInt(5)];
        }else{
            return funMsgNotRaining[rand.nextInt(5)];
        }
    }

    public int getFunImage(boolean bool){
        if(bool){
            return funImgRaining[rand.nextInt(5)];
        }else{
            return funImgNotRaining[rand.nextInt(5)];
        }
    }


}
