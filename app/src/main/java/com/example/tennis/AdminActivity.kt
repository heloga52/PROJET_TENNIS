package com.example.tennis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tennis.model.User
import com.example.tennis.model.UserRole
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*



class AdminActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var users: MutableList<User>
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Set up the Logout button
        val logoutButton: Button = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            // Sign out the user
            auth.signOut()

            // Redirect to SignInActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Charger les utilisateurs de la base de données ou de toute autre source
                users = mutableListOf(
                    User(1, "Alice", UserRole.MEMBER),
                    User(2, "Bob", UserRole.ADHERENT),
                    User(3, "Charlie", UserRole.MEMBER),
                    User(4, "Dave", UserRole.MEMBER),
                    User(5, "Eve", UserRole.ADHERENT)
                )

                // Configurer le RecyclerView pour afficher la liste des utilisateurs
                recyclerView = findViewById(R.id.recyclerView)
                userAdapter = UserAdapter(users)
                recyclerView.adapter = userAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            }

            inner class UserAdapter(private val userList: List<User>) :
                RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

                inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                    val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
                    val roleTextView: TextView = itemView.findViewById(R.id.roleTextView)
                    val memberButton: Button = itemView.findViewById(R.id.button_membre)
                    val adherentButton: Button = itemView.findViewById(R.id.button_adherent)
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                    val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_user, parent, false)
                    return UserViewHolder(itemView)
                }

                override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
                    val currentUser = userList[position]
                    holder.nameTextView.text = currentUser.name
                    holder.roleTextView.text = currentUser.role.toString()

                    if (isAdmin()) {
                        // Permettre à l'administrateur de modifier le rôle de l'utilisateur en appuyant sur les boutons "Membre" ou "Adhérent"
                        holder.memberButton.setOnClickListener {
                            currentUser.role = UserRole.MEMBER
                            holder.roleTextView.text = currentUser.role.toString()
                        }
                        holder.adherentButton.setOnClickListener {
                            currentUser.role = UserRole.ADHERENT
                            holder.roleTextView.text = currentUser.role.toString()
                        }
                    } else {
                        // Désactiver les boutons "Membre" et "Adhérent" pour les utilisateurs non-administrateurs
                        holder.memberButton.isEnabled = false
                        holder.adherentButton.isEnabled = false
                    }
                }

                override fun getItemCount(): Int {
                    return userList.size
                }
            }

            private fun isAdmin(): Boolean {
                // Remplacer ceci par votre vérification de rôle d'administrateur
                return true
            }
}


