package com.mytlx.compiler.syntax.symbol;

import com.mytlx.compiler.syntax.tree.TreeNode;
import com.mytlx.compiler.utils.ToStringUtils;

/**
 * 符号，派生出终结符合非终结符
 *
 * @author TLX
 * @date 2019.5.20
 * @time 21:40
 */
public abstract class Symbol extends ToStringUtils {

    public abstract TreeNode getNode();


}
