package org.straycats.meowthentication.utils

import org.straycats.meowthentication.api.common.Replies
import org.straycats.meowthentication.api.common.Reply

fun <T> List<T>.toReplies(): Replies<T> = Replies(this)
fun <T> T.toReply(): Reply<T> = Reply(this)
