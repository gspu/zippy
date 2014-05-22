/*
 * Copyright (c) 2014, Regents of the University of California
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
package edu.uci.python.parser;

import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.control.*;
import edu.uci.python.nodes.generator.ComprehensionNode.ListComprehensionNode;
import edu.uci.python.nodes.generator.*;
import edu.uci.python.nodes.literal.*;
import edu.uci.python.nodes.profiler.*;
import edu.uci.python.nodes.subscript.*;
import edu.uci.python.runtime.*;

public class ProfilerTranslator implements NodeVisitor {

    private final PythonNodeProber astProber;

    public ProfilerTranslator(PythonContext context) {
        this.astProber = new PythonNodeProber(context);
    }

    public void translate(PythonParseResult parseResult, RootNode root) {
        root.accept(this);

        for (RootNode functionRoot : parseResult.getFunctionRoots()) {
            functionRoot.accept(this);
        }
    }

    @Override
    public boolean visit(Node node) {
        if (node instanceof ListLiteralNode) {
            createWrapperNode((PNode) node);
        } else if (node instanceof TupleLiteralNode) {
            createWrapperNode((PNode) node);
        } else if (node instanceof SetLiteralNode) {
            createWrapperNode((PNode) node);
        } else if (node instanceof DictLiteralNode) {
            createWrapperNode((PNode) node);
        } else if (node instanceof SubscriptLoadIndexNode) {
            createWrapperNode((PNode) node);
        } else if (node instanceof SubscriptStoreIndexNode) {
            createWrapperNode((PNode) node);
        } else if (node instanceof SubscriptLoadSliceNode) {
            createWrapperNode((PNode) node);
        } else if (node instanceof SubscriptStoreSliceNode) {
            createWrapperNode((PNode) node);
        } else if (node instanceof ListComprehensionNode) {
            createWrapperNode((PNode) node);
        }

        return true;
    }

    private PythonWrapperNode createWrapperNode(PNode node) {
        PythonWrapperNode wrapperNode = astProber.probeAsStatement(node);
        node.replace(wrapperNode);
        wrapperNode.adoptChildren();
        return wrapperNode;
    }
}
