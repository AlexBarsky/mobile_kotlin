package ru.mirea.bogomolovaa.mireaproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.mirea.bogomolovaa.mireaproject.databinding.HeroLayoutBinding
import ru.mirea.bogomolovaa.mireaproject.model.Hero

class HeroesAdapter : ListAdapter<Hero, HeroesAdapter.HeroViewHolder>(HeroDiffCallback()) {

    class HeroViewHolder(
        private val binding: HeroLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {

        val image: ImageView = binding.imageHero
        val txt_name: TextView = binding.txtName
        val txt_team: TextView = binding.txtTeam
        val txt_createdby: TextView = binding.txtCreatedby
        val txt_bio: TextView = binding.txtBio

        fun bind(hero: Hero) {
            image.setOnClickListener {
                Toast.makeText(
                    it.context,
                    "Первое появление в ${hero.firstAppearance}г.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.root.setOnClickListener {
                Toast.makeText(
                    it.context,
                    "Нажал на ${txt_name.text}",
                    Toast.LENGTH_SHORT
                ).show()
                txt_bio.text = hero.bio
            }
            // Остальные "бинды"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HeroLayoutBinding.inflate(inflater, parent, false)
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = getItem(position)
        holder.bind(hero)

        Picasso.get().load(hero.imageUrl).into(holder.image)
        holder.txt_name.text = hero.name
        holder.txt_team.text = hero.team
        holder.txt_createdby.text = hero.createdBy
    }
}

class HeroDiffCallback : DiffUtil.ItemCallback<Hero>() {
    override fun areItemsTheSame(oldItem: Hero, newItem: Hero): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Hero, newItem: Hero): Boolean {
        return oldItem == newItem
    }
}