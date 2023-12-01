image:
	docker build -t cs346-server .
	docker run -d -p 8080:8080 cs346-server

tag:
	docker tag cs346-server tk2k2/cs346-server

push:
	docker push tk2k2/cs346-server

pull:
	docker pull tk2k2/cs346-server:latest

run-after-pull:
	docker run -d -p 8080:8080 tk2k2/cs346-server

run:
	java -jar jar_folder/cs346-project.server.main.jar
