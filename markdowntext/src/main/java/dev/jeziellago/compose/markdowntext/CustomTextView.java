package dev.jeziellago.compose.markdowntext;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN) {
            ClickableSpan[] link = getClickableSpans(event);

            if (link.length != 0) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    link[0].onClick(this);
                }
                return true;
            }
        }
        return false;
    }

    private ClickableSpan[] getClickableSpans(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= getTotalPaddingLeft();
        y -= getTotalPaddingTop();

        x += getScrollX();
        y += getScrollY();

        Layout layout = getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        Spannable spannable = (Spannable) getText();
        return spannable.getSpans(off, off, ClickableSpan.class);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
