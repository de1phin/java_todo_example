docker-image:
	ARCH=`uname -m`; \
	docker build -t spring-todo:latest \
		--network=host \
		--build-arg=ARCH=$${ARCH} \
		--build-arg=JAVA_VERSION=21 \
	.
