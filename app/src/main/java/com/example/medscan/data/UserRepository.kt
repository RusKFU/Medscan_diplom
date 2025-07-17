//package com.example.medscan.data
//
//import com.example.medscan.database.dao.UserDao
//import com.example.medscan.database.entity.User
//
//class UserRepository(private val userDao: UserDao) {
//    suspend fun register(user: User): Long = userDao.register(user)
//    suspend fun login(username: String, password: String): User? = userDao.login(username, password)
//}