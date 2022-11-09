package com.example.firebaseiunis_v41

import com.google.firebase.firestore.ListenerRegistration

interface OnProductListener {
    const var firestoreListener: ListenerRegistration

    fun onClick(product: Product)
    fun onLongClick(product: Product)
}