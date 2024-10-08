package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("Petr", "A97f092a", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("Nikolai", "A97f092a", "Ivan", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("Vasya", "A97f092a", null, "Max", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("Max", "A97f092a", null, null, "Vasya"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY,
            WITH_FRIEND,
            WITH_INCOME_REQUEST,
            WITH_OUTCOME_REQUEST
        }
    }

    private Queue<StaticUser> getQueueByUserType(UserType.Type type) {
        switch (type) {
            case EMPTY:
                return EMPTY_USERS;
            case WITH_FRIEND:
                return WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST:
                return WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST:
                return WITH_OUTCOME_REQUEST_USERS;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class) && p.getType().isAssignableFrom(StaticUser.class))
                .forEach(p -> {
                    UserType userType = p.getAnnotation(UserType.class);
                    Queue<StaticUser> queue = getQueueByUserType(userType.value());
                    Optional<StaticUser> staticUser = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();

                    while (staticUser.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        staticUser = Optional.ofNullable(queue.poll());
                    }

                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );
                    staticUser.ifPresentOrElse(
                            u -> {
                                @SuppressWarnings("unchecked")
                                Map<UserType, StaticUser> usersMap = (Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                        .getOrComputeIfAbsent(
                                                context.getUniqueId(),
                                                key -> new HashMap<>()
                                        );

                                usersMap.put(userType, u);
                            },
                            () -> {
                                throw new IllegalStateException("Can't obtain user after 30s.");
                            }
                    );
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        @SuppressWarnings("unchecked")
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );
        if (map != null) {
            for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
                UserType userType = e.getKey();
                StaticUser user = e.getValue();
                Queue<StaticUser> queue = getQueueByUserType(userType.value());
                queue.add(user);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        @SuppressWarnings("unchecked")
        Map<UserType, StaticUser> userMap = (Map<UserType, StaticUser>) extensionContext.getStore(NAMESPACE)
                .getOrComputeIfAbsent(extensionContext.getUniqueId(), key -> new HashMap<>());
        UserType userTypeAnnotation = parameterContext.findAnnotation(UserType.class).orElseThrow(
                () -> new ParameterResolutionException("Аннотация @UserType не найдена"));

        StaticUser staticUser = userMap.get(userTypeAnnotation);
        if (staticUser == null) {
            throw new ParameterResolutionException("Пользователь не найден для указанного типа UserType");
        }
        return staticUser;
    }
}
