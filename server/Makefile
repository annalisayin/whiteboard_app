image:
	docker build -t cs346-server .
	docker run -d -p 8080:8080 cs346-server

tag:
	docker tag cs346-server tk2k2/cs346-server
	docker tag cs346-server tk2k2/cs346-server:sqlite
	docker tag cs346-server tk2k2/cs346-server:postgresql

push:
	docker push tk2k2/cs346-server
	docker push tk2k2/cs346-server:sqlite
	docker push tk2k2/cs346-server:postgresql

pull:
	docker pull tk2k2/cs346-server
	docker pull tk2k2/cs346-server:sqlite
	docker pull tk2k2/cs346-server:postgresql

run-after-pull:
	docker run -d -p 8080:8080 tk2k2/cs346-server

run:
	java -jar jar_folder/cs346-project.server.main.jar
