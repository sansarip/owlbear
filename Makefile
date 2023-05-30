install-ts-npm-modules:
	cd languages/tree-sitter-typescript \
	&& yarn install

build-html-wasm:
	cd languages/tree-sitter-html \
	&& yarn install \
	&& tree-sitter generate \
	&& tree-sitter build-wasm \
	&& cp tree-sitter-html.wasm ../../resources

build-markdown-wasm:
	cd languages/tree-sitter-markdown \
	&& yarn install \
	&& tree-sitter generate \
	&& tree-sitter build-wasm \
	&& cp tree-sitter-markdown.wasm ../../resources
	
build-tsx-wasm: install-ts-npm-modules
	cd languages/tree-sitter-typescript/tsx \
	&& tree-sitter generate \
	&& tree-sitter build-wasm \
	&& cp tree-sitter-tsx.wasm ../../../resources

build-typescript-wasm: install-ts-npm-modules
	cd languages/tree-sitter-typescript/typescript \
	&& tree-sitter generate \
	&& tree-sitter build-wasm \
	&& cp tree-sitter-typescript.wasm ../../../resources
	
update-submodules:
	git submodule update --init --recursive

build-wasms: update-submodules build-html-wasm build-markdown-wasm build-tsx-wasm build-typescript-wasm

cp-tsx-wasm:
	cp languages/tree-sitter-typescript/tsx/tree-sitter-tsx.wasm resources

cp-typescript-wasm:
	cp languages/tree-sitter-typescript/typescript/tree-sitter-typescript.wasm resources

cp-html-wasm:
	cp languages/tree-sitter-html/tree-sitter-html.wasm resources
