package com.housaire.study.algorithms.hard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/1/26 15:00
 * @see
 * @since 1.0.0
 */
public class RegularExpressionMatching
{
    /**
     * <pre>
     * Given an input string (s) and a pattern (p), implement regular expression matching with support for '.' and '*'.
     * '.' Matches any single character.
     * '*' Matches zero or more of the preceding element.
     * The matching should cover the entire input string (not partial).
     *
     * Note:
     *  s could be empty and contains only lowercase letters a-z.
     *  p could be empty and contains only lowercase letters a-z, and characters like . or *.
     *
     * Example 1:
     *  Input:
     *      s = "aa"
     *      p = "a"
     *  Output: false
     *  Explanation: "a" does not match the entire string "aa".
     *
     * Example 2:
     *  Input:
     *      s = "aa"
     *      p = "a*"
     *  Output: true
     *  Explanation: '*' means zero or more of the precedeng element, 'a'. Therefore, by repeating 'a' once, it becomes "aa".
     *
     * Example 3:
     *  Input:
     *      s = "ab"
     *      p = ".*"
     *  Output: true
     *  Explanation: ".*" means "zero or more (*) of any character (.)".
     *
     * Example 4:
     *  Input:
     *      s = "aab"
     *      p = "c*a*b"
     *  Output: true
     *  Explanation: c can be repeated 0 times, a can be repeated 1 time. Therefore it matches "aab".
     *
     * Example 5:
     *  Input:
     *      s = "mississippi"
     *      p = "mis*is*p*."
     *  Output: false
     * </pre>
     *
     * @param s
     * @param p
     * @return
     */
    public static boolean isMatch(String s, String p)
    {
        if ((s == null && null == p) || (null != p && p.equals(".*")))
        {
            return true;
        }
        if ((s == null && null != p) || (s != null && p == null) ||
                (p.indexOf(".") == -1 && p.indexOf("*") == -1 && !s.equals(p)) ||
                (s.trim().length() == 0 && (!p.equals(".") || !p.equals("*") || !p.equals(".*"))))
        {
            return false;
        }
        char[] patterns = p.toCharArray();
        char[] chars = s.toCharArray();
        List<StringBuilder> groupPatterns = new ArrayList<StringBuilder>();
        StringBuilder groupPattern = new StringBuilder(String.valueOf(patterns[0]));
        groupPatterns.add(groupPattern);
        for (int i = 1; i < patterns.length; i++)
        {
            switch (patterns[i])
            {
                case '*':
                    groupPattern.append(patterns[i]);
                    break;
                case '.':
                default:
                    String gps = groupPattern.toString();
                    if (gps.charAt(0) == patterns[i] && gps.indexOf("*") > -1)
                    {
                        break;
                    }
                    groupPattern = new StringBuilder(String.valueOf(patterns[i]));
                    groupPatterns.add(groupPattern);
            }
        }

        int index = 0;
        String lastSuccessPattern = null;
        for (int i = 0; i < groupPatterns.size(); i++)
        {
            String pattern = groupPatterns.get(i).toString();
            switch (pattern)
            {
                case ".*":
                    String nextPattern = null;
                    for (i++; i < groupPatterns.size(); i++)
                    {
                        if (!groupPatterns.get(i).equals(".*") && groupPatterns.get(i).indexOf("*") < 0)
                        {
                            nextPattern = groupPatterns.get(i).toString();
                            break;
                        }
                    }
                    if (null == nextPattern)
                    {
                        return true;
                    }
                    boolean matched = false;
                    for (; index < chars.length; index++)
                    {
                        if (nextPattern.charAt(0) == chars[index])
                        {
                            lastSuccessPattern = nextPattern;
                            matched = true;
                            break;
                        }
                    }
                    if (!matched)
                    {
                        return false;
                    }
                case ".":
                    if (index < chars.length)
                    {
                        lastSuccessPattern = pattern;
                        index++;
                        break;
                    }
                    else if (null == lastSuccessPattern || lastSuccessPattern.indexOf("*") < 0)
                    {
                        return false;
                    }
                case "*":
                    return false;
                default:
                    char prefix = pattern.charAt(0);
                    boolean keepMatch = pattern.contains("*");
                    if (index >= chars.length)
                    {
                        if (keepMatch || (null != lastSuccessPattern && lastSuccessPattern.indexOf("*") > -1 && lastSuccessPattern.charAt(0) == prefix))
                        {
                            break;
                        }
                        else
                        {
                            return false;
                        }
                    }

                    for (; index < chars.length; )
                    {
                        if (prefix == chars[index])
                        {
                            index++;
                            lastSuccessPattern = pattern;
                            if (!keepMatch)
                            {
                                break;
                            }
                        }
                        else if (keepMatch)
                        {
                            break;
                        }
                        else
                        {
                            return false;
                        }
                    }
            }
        }
        return index < chars.length ? false : true;
    }

    public static void main(String[] args)
    {
        System.out.println(isMatch("a", "b.*..a*"));
    }

}
