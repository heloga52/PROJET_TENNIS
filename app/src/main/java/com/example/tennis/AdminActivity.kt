package com.example.tennis

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
import com.google.firebase.database.*



class AdminActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: UsersListAdapter
    private lateinit var usersList: RecyclerView
    private lateinit var usersRef: DatabaseReference

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

