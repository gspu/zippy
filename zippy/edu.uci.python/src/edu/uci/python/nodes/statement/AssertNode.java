/*
 * Copyright (c) 2013, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.uci.python.nodes.statement;

import org.python.core.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;

import edu.uci.python.ast.VisitorIF;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.expression.*;
import edu.uci.python.runtime.datatype.*;

public class AssertNode extends StatementNode {

    @Child protected CastToBooleanNode condition;
    @Child protected PNode message;

    public AssertNode(CastToBooleanNode condition, PNode message) {
        this.condition = condition;
        this.message = message;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (CompilerDirectives.inInterpreter()) {
            if (!condition.executeBoolean(frame)) {
                String assertionMessage = message == null ? "" : (String) message.execute(frame);
                throw Py.AssertionError(assertionMessage);
            }
        }

        return PNone.NONE;
    }

    public CastToBooleanNode getCondition() {
        return condition;
    }

    public PNode getMessage() {
        return message;
    }

    @Override
    public <R> R accept(VisitorIF<R> visitor) throws Exception {
        return visitor.visitAssertNode(this);
    }

}
