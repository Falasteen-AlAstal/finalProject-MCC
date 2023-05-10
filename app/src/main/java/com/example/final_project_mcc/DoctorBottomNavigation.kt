package com.example.final_project_mcc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_doctor_bottom_navigation.doctor_bottom_navigation

class DoctorBottomNavigation : AppCompatActivity(),   BottomNavigationView.OnNavigationItemSelectedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_bottom_navigation)
       doctor_bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

    var name = intent.getStringExtra("PAGE")

    if (name != null) {
        if (name.equals("Chat", ignoreCase = true)) {
            loadcheffragment(FGChatDoctor())
        } else if (name.equals("Profile", ignoreCase = true)) {
            loadcheffragment(FGProfileDoctor())
        } else if (name.equals("notification", ignoreCase = true)) {
            loadcheffragment(notification())
        } else if (name.equals("Home", ignoreCase = true)) {
            loadcheffragment(FGHomeDoctor())
        }
    } else {
        loadcheffragment(FGHomeDoctor())
    }

}

override fun onNavigationItemSelected(item: MenuItem): Boolean {
    var fragment: Fragment? = null
    when (item.itemId) {
        R.id.doctorHome -> fragment = FGHomeDoctor() //ChefPendingOrderFragment();
        R.id.chtadoctor -> fragment = FGChatDoctor()
        R.id.profil1 -> fragment = FGProfileDoctor()
        R.id.notification -> fragment = notification()
    }
    return loadcheffragment(fragment)
}

private fun loadcheffragment(fragment: Fragment?): Boolean {
    if (fragment != null) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
            .commit()
        return true
    }
    return false
}
}
