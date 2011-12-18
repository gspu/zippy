/*
 * Copyright (c) 2011, 2011, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.max.graal.snippets.target.amd64;

import com.oracle.max.asm.target.amd64.*;
import com.oracle.max.graal.compiler.asm.*;
import com.oracle.max.graal.compiler.lir.*;
import com.oracle.max.graal.compiler.target.amd64.*;
import com.oracle.max.graal.compiler.util.*;
import com.sun.cri.ci.*;

public enum AMD64MathIntrinsicOpcode implements LIROpcode {
    SQRT,
    SIN, COS, TAN,
    LOG, LOG10;

    public LIRInstruction create(CiVariable result, CiVariable input) {
        CiValue[] inputs = new CiValue[] {input};

        return new AMD64LIRInstruction(this, result, null, inputs, LIRInstruction.NO_OPERANDS, LIRInstruction.NO_OPERANDS) {
            @Override
            public void emitCode(TargetMethodAssembler tasm, AMD64MacroAssembler masm) {
                emit(tasm, masm, tasm.asDoubleReg(result()), tasm.asDoubleReg(input(0)));
            }
        };
    }

    /**
     * @param tasm
     */
    private void emit(TargetMethodAssembler tasm, AMD64MacroAssembler masm, CiRegister result, CiRegister input) {
        switch (this) {
            case SQRT:  masm.sqrtsd(result, input); break;
            case LOG:   masm.flog(result, input, false); break;
            case LOG10: masm.flog(result, input, true); break;
            case SIN:   masm.fsin(result, input); break;
            case COS:   masm.fcos(result, input); break;
            case TAN:   masm.ftan(result, input); break;
            default:    throw Util.shouldNotReachHere();
        }
    }
}
