
package javaapplication9;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;


public class Music {
 
    private static final String[] choice = { "file:sound/putChess.wav",
        "file:sound/win.wav", "file:sound/lose.wav" }; // 声音文件名数组,

    URL url;
    AudioClip adc;// 声音音频剪辑对象

    
    public void SetPlayAudioPath(String path){
        
        try{  
        
            url = new URL(path);  
        
            // System.out.println(adc.toString());
        
        }   catch (MalformedURLException e1) {
        
            System.out.println("找不到文件");  
        
	}
        
        adc = Applet.newAudioClip(url);
        
    }
    
    void play(){     //开始播放
        
        adc.play();
        
    }   
    
    void stop(){     //停止播放
        
	adc.stop(); 

    }

 

	/**

	 * 落子声音

	 */

	public void putVoice() {
            
            SetPlayAudioPath(choice[0]);
            
            play();
            
        }
 

	/**

	 * 获胜声音

	 */

    public void winVoice() {

        SetPlayAudioPath(choice[1]);
            
        play();
    }



 

	/**

	 * 失败声音

	 */

	public void lostVoice() {

            SetPlayAudioPath(choice[2]);
            
            play();

	}

}
