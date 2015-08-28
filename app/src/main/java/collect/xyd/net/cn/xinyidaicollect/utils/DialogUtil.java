package collect.xyd.net.cn.xinyidaicollect.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import collect.xyd.net.cn.xinyidaicollect.R;

public class DialogUtil {
    public static Dialog createLoadingDialog(Context context, String str) {
        View localObject = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
        LinearLayout localLinearLayout = (LinearLayout)localObject.findViewById(R.id.dialog_view);
        ImageView localImageView = (ImageView)localObject.findViewById(R.id.img);
        TextView localTextView = (TextView)localObject.findViewById(R.id.tipTextView);
        localImageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_animation));
        localTextView.setText(str);
        Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);
        dialog.setContentView(localLinearLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return dialog;
    }
}