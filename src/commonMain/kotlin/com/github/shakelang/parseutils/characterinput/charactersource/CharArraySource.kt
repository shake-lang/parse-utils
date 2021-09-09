package com.github.shakelang.parseutils.characterinput.charactersource

/**
 * A [CharArraySource] is an implementation of [CharacterSource] that provides the characters based
 * on a [CharArray]
 *
 * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
 *
 * @see CharacterSource
 */
internal class CharArraySource (

    /**
     * The contents of the [CharacterSource] (implementation of [CharacterSource.all])
     *
     * @see CharacterSource.all
     */
    override val all: CharArray,

    /**
     * The location the characters are from (e.g. file location) (implementation of [CharacterSource.location])
     *
     * @see CharacterSource.location
     */
    override val location: String

) : CharacterSource {

    /**
     * The length of the [CharacterSource] (implementation of [CharacterSource.length])
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;]
     *
     * @see CharacterSource.length
     */
    override val length: Int get() = all.size

    /**
     * Get a range of characters from the [CharacterSource] as a [CharArray]
     * (implementation of [CharacterSource.get])
     *
     * @param start the start index for the characters
     * @param end the end index for the characters
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     *
     * @see CharacterSource.get
     */
    override fun get(start: Int, end: Int): CharArray = all.copyOfRange(start, end)
}