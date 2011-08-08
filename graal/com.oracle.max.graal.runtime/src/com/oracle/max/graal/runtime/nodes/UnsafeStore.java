/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.max.graal.runtime.nodes;

import com.oracle.max.graal.compiler.ir.*;
import com.oracle.max.graal.compiler.phases.*;
import com.oracle.max.graal.compiler.phases.LoweringPhase.*;
import com.oracle.max.graal.graph.*;
import com.sun.cri.ci.*;

/**
 * Store of a value at a location specified as an offset relative to an object.
 */
public class UnsafeStore extends StateSplit {

    @Input    private Value object;

    @Input    private Value offset;

    @Input    private Value value;

    public Value object() {
        return object;
    }

    public void setObject(Value x) {
        updateUsages(object, x);
        object = x;
    }

    public Value offset() {
        return offset;
    }

    public void setOffset(Value x) {
        updateUsages(offset, x);
        offset = x;
    }

    public Value value() {
        return value;
    }

    public void setValue(Value x) {
        updateUsages(value, x);
        value = x;
    }

    public UnsafeStore(Value object, Value offset, Value value, CiKind kind, Graph graph) {
        super(kind, graph);
        setObject(object);
        setOffset(offset);
        setValue(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Op> T lookup(Class<T> clazz) {
        if (clazz == LoweringOp.class) {
            return (T) LoweringPhase.DELEGATE_TO_RUNTIME;
        }
        return super.lookup(clazz);
    }
}
