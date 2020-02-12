package cash.z.ecc.android.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import cash.z.ecc.android.R
import cash.z.ecc.android.databinding.FragmentSendMemoBinding
import cash.z.ecc.android.di.viewmodel.activityViewModel
import cash.z.ecc.android.ext.*
import cash.z.ecc.android.ui.base.BaseFragment

class SendMemoFragment : BaseFragment<FragmentSendMemoBinding>() {

    val sendViewModel: SendViewModel by activityViewModel()

    override fun inflate(inflater: LayoutInflater): FragmentSendMemoBinding =
        FragmentSendMemoBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNext.setOnClickListener {
            onTopButton()
        }
        binding.buttonSkip.setOnClickListener {
            onBottomButton()
        }
        binding.clearMemo.setOnClickListener {
            onClearMemo()
        }

        R.id.action_nav_send_memo_to_nav_send_address.let {
            binding.backButtonHitArea.onClickNavTo(it)
            onBackPressNavTo(it)
        }

        binding.checkIncludeAddress.setOnCheckedChangeListener { _, _->
            onIncludeMemo(binding.checkIncludeAddress.isChecked)
        }

        binding.inputMemo.let { memo ->
            memo.onEditorActionDone {
                onTopButton()
            }
            memo.doAfterTextChanged { 
                binding.clearMemo.goneIf(memo.text.isEmpty())
            }
        }

        sendViewModel.afterInitFromAddress {
            binding.textIncludedAddress.text = "sent from ${sendViewModel.fromAddress}"
        }

        binding.textIncludedAddress.gone()

        applyModel()
    }

    private fun onClearMemo() {
        binding.inputMemo.setText("")
    }

    private fun applyModel() {
        sendViewModel.isShielded.let { isShielded ->
            binding.groupShielded.goneIf(!isShielded)
            binding.groupTransparent.goneIf(isShielded)
            binding.textIncludedAddress.goneIf(!sendViewModel.includeFromAddress)
            if (isShielded) {
                binding.inputMemo.setText(sendViewModel.memo)
                binding.checkIncludeAddress.isChecked = sendViewModel.includeFromAddress
                binding.buttonNext.text = "ADD MEMO"
                binding.buttonSkip.text = "OMIT MEMO"
            } else {
                binding.buttonNext.text = "GO BACK"
                binding.buttonSkip.text = "PROCEED"
            }
        }
    }

    private fun onIncludeMemo(checked: Boolean) {
        binding.textIncludedAddress.goneIf(!checked)
        sendViewModel.includeFromAddress = checked
        binding.textInfoShielded.text = if (checked) {
            getString(R.string.send_memo_included_message)
        } else {
            getString(R.string.send_memo_excluded_message)
        }
    }

    private fun onTopButton() {
        if (sendViewModel.isShielded) {
            sendViewModel.memo = binding.inputMemo.text.toString()
            onNext()
        } else {
            mainActivity?.safeNavigate(R.id.action_nav_send_memo_to_nav_send_address)
        }
    }

    private fun onBottomButton() {
        binding.inputMemo.setText("")
        sendViewModel.memo = ""
        sendViewModel.includeFromAddress = false
        onNext()
    }

    private fun onNext() {
        mainActivity?.safeNavigate(R.id.action_nav_send_memo_to_send_confirm)
    }
}