package com.sanjana.gujjar.nycschools.nycdir.nycschoollist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.databinding.NycSchoolListRowItemBinding
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.School
import com.sanjana.gujjar.nycschools.utils.FormatUtils
import com.sanjana.gujjar.nycschools.utils.colorSpannableStringWithUnderLineOne

class SchoolDetailsAdapter(
    schoolListFragment: SchoolListFragment,
    private val detailsClickListener: (selectedItem: School) -> Unit,
) : RecyclerView.Adapter<SchoolDetailsAdapter.SchoolItemViewHolder>() {

    private var list: List<School> = mutableListOf()
    private var context = schoolListFragment

    fun updateSchoolList(updatedList: List<School>) {
        list = updatedList
        notifyDataSetChanged()
    }

    class SchoolItemViewHolder(val binding: NycSchoolListRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolItemViewHolder {
        return SchoolItemViewHolder(
            NycSchoolListRowItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SchoolItemViewHolder, position: Int) {
        val school = list[position]
        with(holder.binding) {
            nycName.text = context.resources.getString(R.string.full_name,school.schoolName)
            nycWebsite.colorSpannableStringWithUnderLineOne(
                context.resources.getString(R.string.website),
                school.website ?:  "",
                callback = {
                    //TODO
                })
            nycWebsite.invalidate()
            location.text = context.resources.getString(R.string.biography,school.location)
            empEmailAddress.colorSpannableStringWithUnderLineOne(
                context.resources.getString(R.string.email_address),
                school.emailAddress ?:  "",
                callback = { //TODO
                    })
            empEmailAddress.invalidate()
            empPhoneNumber.colorSpannableStringWithUnderLineOne(
                context.resources.getString(R.string.phone_number),
                FormatUtils.formatNumber(school.phoneNumber) ?:  "",
                callback = { //TODO
                })
            empPhoneNumber.invalidate()
            nycType.text = context.resources.getString(R.string.fax,school.faxNumber)
        }
        holder.binding.root.setOnClickListener{
            detailsClickListener(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}