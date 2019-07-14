package com.niken.tkalfiizzah.fragment.profile;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseFragment;
import com.niken.tkalfiizzah.databinding.FragmentProfileBinding;

/**
 * Created by Firman on 4/17/2019.
 */
public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> {

    private ProfileViewModel viewModel;

    @Override
    protected int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected ProfileViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new ProfileViewModel(getActivity());
        return viewModel;
    }
}
