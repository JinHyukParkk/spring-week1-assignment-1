package com.codesoom.assignment;

import com.codesoom.HttpEnum.HttpMethodCode;
import com.codesoom.models.SendResponseData;
import com.codesoom.models.TaskService;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CrudHttpHandler implements HttpHandler {

    private static final TaskService TASK_SERVICE_IMPL = new TaskService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        final String method = exchange.getRequestMethod();
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        // 라이브러리 메서드가 반환할 결과값이 ‘없음’을 명백하게 표현할 필요가 있는 곳에서 제한적으로 사용할 수 있는 메커니즘을 제공하는 것이 Optional을 만든 의도라고 나와았습니다.
        // 옵셔널의 사용이 어제가 처음이여서 그런지 많은 어색함이 있는것 같습니다.주의사항이 여러개 있었는데 그중 하나가 옵셔널을 필드에서 사용하면 좋지 않다는게 문지점인것 같습니다.
        // 하지만 수정에 대한 감이 잡히질 않아 옵셔널에 좀 더 익숙해질 필요가 있어 다양하게 적용해 보고 있습니다.

        /*
         * method에 null이 들어온다면 비교주체자가 null이 되버리기 때문에 equals를 실행할 수 없어 NPE가 발생할 가능성이 생기는 것 같습니다.
         * 반대로 "GET".equals(method) 로 변경하면 비교하는 주체가 null이 발생할 일이 없어지기 때문에 NPE 방지가 되는 원리 같습니다.
         */
        if( getAllMethod(method, path) ) {

            SendResponseData response = TASK_SERVICE_IMPL.getAll();

            TASK_SERVICE_IMPL.sendResponse(response,exchange);
            return;

        } else if(getDetailMethod(method, path)){

            Optional<String> hasId = hasId(path);
            SendResponseData response = TASK_SERVICE_IMPL.getOne(hasId.get());

            TASK_SERVICE_IMPL.sendResponse(response,exchange);
            return;

        } else if(postMethod(method, path)) {

            SendResponseData response = TASK_SERVICE_IMPL.join(getContent(exchange));

            TASK_SERVICE_IMPL.sendResponse(response,exchange);
            return;

        } else if(updateMethod(method, path)) {

            Optional<String> hasId = hasId(path);
            SendResponseData response = TASK_SERVICE_IMPL.edit(getContent(exchange), hasId.get());

            TASK_SERVICE_IMPL.sendResponse(response,exchange);
            return;

        } else if(deleteMethod(method, path)) {

            Optional<String> hasId = hasId(path);
            SendResponseData response = TASK_SERVICE_IMPL.delete(hasId.get());

            TASK_SERVICE_IMPL.sendResponse(response,exchange);
            return;

        }

    }


    private boolean getAllMethod(String method, String path) {
        return HttpMethodCode.GET.getStatus().equals(method) && "/tasks".equals(path) && !hasId(path).isPresent();
    }

    private boolean getDetailMethod(String method, String path) {
        return HttpMethodCode.GET.getStatus().equals(method)  && hasId(path).isPresent();
    }

    private boolean postMethod(String method, String path) {
        return HttpMethodCode.POST.getStatus().equals(method) && "/tasks".equals(path);
    }

    private boolean updateMethod(String method, String path) {
        return (HttpMethodCode.PUT.getStatus().equals(method) || HttpMethodCode.PATCH.getStatus().equals(method)) && hasId(path).isPresent();
    }
    private boolean deleteMethod(String method, String path) {
        return HttpMethodCode.DELETE.getStatus().equals(method) && hasId(path).isPresent();
    }


    private Optional hasId(String path) {

        Optional<String> taskId = Optional.empty();
        if ( Pattern.matches("/tasks/[0-9]*$",path) ) {
            return Optional.of(path.replace("/tasks/", ""));
        }
        return taskId;
    }

    private String getContent(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }


}