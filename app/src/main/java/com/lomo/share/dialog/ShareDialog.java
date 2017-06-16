package com.lomo.share.dialog;


import android.app.Activity;

import com.lomo.R;
import com.lomo.databinding.DialogShareBinding;
import com.lomo.share.ShareEntity;
import com.lomo.share.ShareListener;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ShareDialog extends BaseBottomDialog<DialogShareBinding> {

    private ShareDialogViewModel mViewModel;
    private ShareData mShareData;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share;
    }

    @Override
    public void initView() {
        mViewModel = new ShareDialogViewModel(
                (Activity) getActivity(),
                this,
                mShareData.getShareEntiry(),
                mShareData.getShareListener());
        bindingView.setShareDialogViewModel(mViewModel);
    }

    public interface ShareData {
        ShareEntity getShareEntiry();
        ShareListener getShareListener();
    }

    public void setShareData(ShareData shareData) {
        this.mShareData = shareData;
    }
}