package jb.production.fitsleep.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import jb.production.fitsleep.BR

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    private lateinit var _binding: VB
    protected val binding get() = _binding

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = DataBindingUtil.inflate(layoutInflater, getLayout(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel()?.let {
            binding.setVariable(BR.viewModel, it)
            binding.lifecycleOwner = this
        }
    }

    @LayoutRes
    abstract fun getLayout(): Int
    abstract fun getViewModel(): ViewModel?
}