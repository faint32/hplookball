package com.hupu.games.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.hupu.games.common.HupuLog;
/**
 * 
 * @author zhenhua
 *
 */
public class AutoTextView  extends TextView {

	private Paint paint;
	private float cTextSize;
	private CharSequence preText="";

	public AutoTextView (Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.set(this.getPaint());
	}
	/**
	 * 将文字缩放到合适大大小
	 * @param maxLineText 文本中最长的一行的长度
	 * @param viewWidth TextView的宽度
	 * @param linenum 文本的行数
	 */
	private void refitText(String maxLineText,String allText,int linenum) {
		int viewWidth=this.getWidth();
		int viewHeight=this.getHeight();
		
		HupuLog.e(this.getClass().getName(), "refitText===text="+maxLineText+",viewWidth="+viewWidth+",viewHeight="+viewHeight+",linenum="+linenum);
		if (viewWidth > 0) {
			//获得当前TextView的有效宽度
			int widthspace=10;
			int heightspace=10;
			if(linenum==2 ){
				widthspace=30;
				heightspace=30;
			}else if(linenum==1){
				widthspace=15;
				heightspace=20;
				if(allText.length()==1){//1个字符
					widthspace=60;
					heightspace=60;
				}else if(allText.length()==2){//2个字符
					widthspace=30;
					heightspace=30;
				}
			}
			int availableWidth = viewWidth - this.getPaddingLeft() - this.getPaddingRight()-widthspace;
			int availableHeight = viewHeight - this.getPaddingTop() - this.getPaddingBottom()-heightspace;
			HupuLog.e(this.getClass().getName(), "availableWidth="+availableWidth+",availableHeight = "+availableHeight);
			float[] widths = new float[maxLineText.length()];
			Rect rect = new Rect();
			paint.getTextBounds(maxLineText, 0, maxLineText.length(), rect);
			//所有字符串所占像素宽度
//			int textWidths = rect.width();
			float textWidths = getTextWidth(paint,maxLineText);
			float textHeight = rect.height()*linenum;
			
			HupuLog.e(this.getClass().getName(), "Rect==="+rect);
			
			cTextSize = this.getTextSize();//这个返回的单位为px
			HupuLog.e(this.getClass().getName(), "befor change === cTextSizepre="+cTextSize+",textWidths="+textWidths+",textHeight="+textHeight+",paint.size="+paint.getTextSize());

			if(textWidths > availableWidth || textHeight > availableHeight){
				while(textWidths > availableWidth ||  textHeight > availableHeight){//大了变小
					HupuLog.e(this.getClass().getName(), "宽高变小中 = textWidths="+textWidths+",textHeight="+textHeight);
					cTextSize = cTextSize - 1;
					float[] wh = textChange(maxLineText, widths,rect,linenum);
					textWidths=wh[0];
					textHeight=wh[1];
				}
			}else if(textWidths < availableWidth && textHeight < availableHeight){
				while(textWidths < availableWidth && textHeight < availableHeight){//变小大了
					HupuLog.e(this.getClass().getName(), "宽度变大中  = textWidths="+textWidths+",textHeight="+textHeight);
					cTextSize = cTextSize + 1;
					float[] wh = textChange(maxLineText, widths,rect,linenum);
					textWidths=wh[0];
					textHeight=wh[1];
				}
				//微调
				if(textWidths > availableWidth-4 || textHeight > availableHeight-4){
					HupuLog.e(this.getClass().getName(), "微调前 = textWidths="+textWidths+",textHeight="+textHeight+"cTextSize="+cTextSize);
					cTextSize = cTextSize - 1;
					float[] wh = textChange(maxLineText, widths,rect,linenum);
					textWidths=wh[0];
					textHeight=wh[1];	
				}
				HupuLog.e(this.getClass().getName(), "微调后 = textWidths="+textWidths+",textHeight="+textHeight+","+cTextSize);
			}

			HupuLog.e(this.getClass().getName(), "cTextSize_after="+cTextSize);
			this.setTextSize(TypedValue.COMPLEX_UNIT_PX, cTextSize);//这里制定传入的单位是px
		}
	};
	private float[] textChange(String maxLineText,float[] widths,Rect rect,int linenum){
		HupuLog.e(this.getClass().getName(), "befor textchage = "+paint.getTextSize()+","+paint.measureText(maxLineText));
		float[] wh=new float[2];
		
		setTextSize(TypedValue.COMPLEX_UNIT_PX, cTextSize);//这里制定传入的单位是px
		paint.setTextSize(cTextSize);//这里传入的单位是px
		
		
		rect = new Rect();
		paint.getTextBounds(maxLineText, 0, maxLineText.length(), rect);
//		所有字符串所占像素宽度
//		int textWidths = rect.width();
		float textWidths=getTextWidth(paint,maxLineText);
//		int textWidths = paint.getTextWidths(text, widths);
		float textHeight = rect.height()*linenum;
		wh[0]=textWidths;
		wh[1]=textHeight;
		
		HupuLog.e(this.getClass().getName(), "after textchage = "+paint.getTextSize()+","+paint.measureText(maxLineText)+","+textWidths);
		return wh;
	}
	private  float getTextWidth(Paint paint, String str) {  
		float iRet = 0;  
        if (str != null && str.length() > 0) {  
            int len = str.length();  
           /* float[] widths = new float[len];  
            paint.getTextWidths(str, widths);  
            for (int j = 0; j < len; j++) {  
                iRet += (int) Math.ceil(widths[j]);  
            } */ 
            iRet =  paint.getTextSize()*str.length();
        }  
        return iRet;  
    }  
	/**
	 * 取出多行数据中最长的一行来做缩放
	 * @param text
	 * @param textWidth textview长度
	 */
	private void refitAvalableText(String text, int viewWidth){
		HupuLog.e(this.getClass().getName(), "refitAvalableText====>viewWidth="+viewWidth+",text="+text);
		String[] linetext = text.split("\n");
		int maxWidth=0;
		int usefullline=-1;
		if(linetext != null && linetext.length>0){
			for(int i=0;i<linetext.length;i++){

				Rect rect = new Rect();
				paint.getTextBounds(linetext[i], 0, linetext[i].length(), rect);
				int lineWidth =rect.width();
				HupuLog.e(this.getClass().getName(), "lineWidth="+lineWidth+",maxWidth="+maxWidth);
				if(lineWidth>maxWidth){
					maxWidth = lineWidth;
					usefullline=i;
				}

			}
			if(usefullline>=0){
				refitText(linetext[usefullline],text,linetext.length);
			}
		}
	
	}

	@Override
	protected void onDraw(Canvas canvas) {	
		if(!getText().equals(preText)){
			refitAvalableText(getText().toString(), getWidth());
			preText = getText();
		}
		super.onDraw(canvas);
	}
}