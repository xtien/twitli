/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.twit

import twitter4j.*
import java.util.*

class StatusImpl(t: String) : Status {

    private val text: String = t

    override fun getUserMentionEntities(): Array<UserMentionEntity> {
        TODO("Not yet implemented")
    }

    override fun getContributors(): LongArray {
        TODO("Not yet implemented")
    }

    override fun isFavorited(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getInReplyToScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun getDisplayTextRangeStart(): Int {
        TODO("Not yet implemented")
    }

    override fun getGeoLocation(): GeoLocation {
        TODO("Not yet implemented")
    }

    override fun getSource(): String {
        TODO("Not yet implemented")
    }

    override fun getInReplyToStatusId(): Long {
        TODO("Not yet implemented")
    }

    override fun getId(): Long {
        TODO("Not yet implemented")
    }

    override fun getWithheldInCountries(): Array<String> {
        TODO("Not yet implemented")
    }

    override fun getCurrentUserRetweetId(): Long {
        TODO("Not yet implemented")
    }

    override fun getSymbolEntities(): Array<SymbolEntity> {
        TODO("Not yet implemented")
    }

    override fun getText(): String {
        return text
    }

    override fun getAccessLevel(): Int {
        TODO("Not yet implemented")
    }

    override fun getInReplyToUserId(): Long {
        TODO("Not yet implemented")
    }

    override fun getMediaEntities(): Array<MediaEntity> {
        TODO("Not yet implemented")
    }

    override fun getPlace(): Place {
        TODO("Not yet implemented")
    }

    override fun isRetweetedByMe(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUser(): User {
        TODO("Not yet implemented")
    }

    override fun getURLEntities(): Array<URLEntity> {
        TODO("Not yet implemented")
    }

    override fun isRetweeted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLang(): String {
        TODO("Not yet implemented")
    }

    override fun getQuotedStatus(): Status {
        TODO("Not yet implemented")
    }

    override fun getRateLimitStatus(): RateLimitStatus {
        TODO("Not yet implemented")
    }

    override fun getDisplayTextRangeEnd(): Int {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: Status?): Int {
        TODO("Not yet implemented")
    }

    override fun getQuotedStatusId(): Long {
        TODO("Not yet implemented")
    }

    override fun isRetweet(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getRetweetedStatus(): Status {
        TODO("Not yet implemented")
    }

    override fun getFavoriteCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isPossiblySensitive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getScopes(): Scopes {
        TODO("Not yet implemented")
    }

    override fun isTruncated(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCreatedAt(): Date {
        TODO("Not yet implemented")
    }

    override fun getQuotedStatusPermalink(): URLEntity {
        TODO("Not yet implemented")
    }

    override fun getHashtagEntities(): Array<HashtagEntity> {
        TODO("Not yet implemented")
    }

    override fun getRetweetCount(): Int {
        TODO("Not yet implemented")
    }

}
