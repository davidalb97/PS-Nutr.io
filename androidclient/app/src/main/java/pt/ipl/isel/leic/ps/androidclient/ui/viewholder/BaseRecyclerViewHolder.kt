package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IContext
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ISend
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

/**
 * A generic View Holder for [RecyclerView.ViewHolder]
 */
abstract class BaseRecyclerViewHolder<T : Any>(
    val navDestination: Navigation,
    val view: View,
    val ctx: Context
) : RecyclerView.ViewHolder(view), ISend, IContext, IItemClickListenerOwner<T> {

    override val log: Logger by lazy { Logger(javaClass) }
    lateinit var item: T

    override var onClickListener: IItemClickListener<T>? = IItemClickListener { _, _ ->
        log.v("Clicked item at index $layoutPosition")
        sendToDestination(view, navDestination)
    }

    init {
        view.setOnClickListener {
            onClickListener?.onClick(item) {
                reRender()
            }
        }
    }

    open fun bindTo(item: T) {
        this.item = item
    }

    fun reRender() = bindingAdapter?.notifyItemChanged(layoutPosition)

    override fun fetchCtx(): Context = ctx
}