package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.InsulinProfileRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel

class InsulinProfilesRecyclerFragment :
    ARoomRecyclerListFragment<InsulinProfile, InsulinProfilesRecyclerViewModel>() {

    private val adapter: InsulinProfileRecyclerAdapter by lazy {
        InsulinProfileRecyclerAdapter(
            viewModel,
            this.requireContext()
        )
    }

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = InsulinProfilesVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel = ViewModelProvider(rootActivity, factory)[InsulinProfilesRecyclerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.insulin_profiles_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerList(view)
        setCallbackFunctions()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        startObserver()
        viewModel.updateListFromLiveData()
        // Retrieve button to add an insulin profile
        val addButton =
            view.findViewById<ImageButton>(R.id.add_profile)

        // Setup a listener to go to the fragment that adds a profile
        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.nav_add_insulin)
        }
    }

    override fun startScrollListener() {
        TODO("Not yet implemented")
    }


    /* override fun startScrollListener() {
         TODO("Not yet implemented")
     }*/

    /*fun <T> deleteListener(
        view: View,
        position: Int,
        element: T,
        elementList: MutableList<T>,
        deleteFunction: (T) -> Unit,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ): Boolean {

        val builder = AlertDialog.Builder(view.context)

        builder.setTitle(view.context.getString(R.string.DialogAlert_Warning))

        builder.setMessage(view.context.getString(R.string.AlertDialog_deleteQuestion))

        builder.setPositiveButton(view.context.getString(R.string.DialogAlert_Yes)) { _, _ ->

            deleteFunction(element)
            Toast.makeText(
                view.context,
                view.context.getString(R.string.DialogAlert_deleted), Toast.LENGTH_SHORT
            ).show()
            elementList.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyDataSetChanged()
        }

        builder.setNegativeButton(view.context.getString(R.string.Dialog_no)) { _, _ -> }

        val dialog: AlertDialog = builder.create()

        dialog.show()

        return true
    }*/
}