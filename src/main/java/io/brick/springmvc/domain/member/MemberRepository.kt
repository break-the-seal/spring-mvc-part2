package io.brick.springmvc.domain.member

import mu.KLogging
import org.springframework.stereotype.Repository

@Repository
class MemberRepository {

    companion object: KLogging() {
        val store: MutableMap<Long, Member> = mutableMapOf()
        var sequence: Long = 0L
    }

    fun save(member: Member): Member {
        member.id = ++sequence
        logger.info { "save: member = $member" }
        store[member.id] = member

        return member
    }

    fun findById(id: Long): Member? {
        return store[id]
    }

    fun findAll(): List<Member> {
        return store.values.toList()
    }

    fun findByLoginId(loginId: String): Member? {
        return findAll().find { it.loginId == loginId }
    }

    fun clear() {
        store.clear()
    }
}