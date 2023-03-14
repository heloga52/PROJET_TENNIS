package com.example.tennis

<<<<<<< Updated upstream
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
=======
import com.example.tennis.UserRole
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tennis.model.UserRole
import com.google.firebase.auth.FirebaseAuth
>>>>>>> Stashed changes
import com.google.firebase.database.*



class AdminActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
<<<<<<< Updated upstream
    private lateinit var users: MutableList<User>
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
=======
    private lateinit var adapter: UsersListAdapter
    private lateinit var usersList: RecyclerView
    private lateinit var usersRef: DatabaseReference
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
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

=======
        usersList = findViewById(R.id.users_list)
        usersList.layoutManager = LinearLayoutManager(this)

        // Récupération de la référence de la base de données Firebase
        usersRef = FirebaseDatabase.getInstance().getReference("users")

        // Récupération des utilisateurs de la base de données Firebase
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (userSnapshot in snapshot.children) {
                    val email = userSnapshot.child("email").getValue(String::class.java)
                    val roleValue = userSnapshot.child("role").getValue()
                    val role = when(roleValue){
                        is Boolean -> if (roleValue) UserRole.ADMIN.toString() else UserRole.member.toString()
                        is String -> roleValue
                        else -> null
                    }
                    val uid = userSnapshot.key
                    if (email != null && role != null && uid != null) {
                        users.add(User(uid, email, role))
                    }
                }
                adapter = UsersListAdapter(users)
                usersList.adapter = adapter
            }


            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error fetching users", error.toException())
            }
        })
    }

    companion object {
        private const val TAG = "AdminActivity"
    }
}

class UsersListAdapter(private val users: List<User>) :
    RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val emailTextView: TextView = view.findViewById(R.id.email_text_view)
        private val roleRadioGroup: RadioGroup = view.findViewById(R.id.role_radio_group)
        private val saveButton: Button = view.findViewById(R.id.save_button)

        fun bind(user: User) {
            emailTextView.text = user.email
            when (user.role) {
                UserRole.member.toString() -> roleRadioGroup.check(R.id.member_radio_button)
                UserRole.adherent.toString() -> roleRadioGroup.check(R.id.adherent_radio_button)
            }
            saveButton.setOnClickListener {
                val newRole = when (roleRadioGroup.checkedRadioButtonId) {
                    R.id.member_radio_button -> UserRole.member.toString()
                    R.id.adherent_radio_button -> UserRole.adherent.toString()
                    else -> ""
                }
                if (newRole.isNotEmpty()) {
                    val usersRef = FirebaseDatabase.getInstance().getReference("users")
                    usersRef.child(user.uid).child("role").setValue(newRole)
                }
            }
        }
    }

}
>>>>>>> Stashed changes

