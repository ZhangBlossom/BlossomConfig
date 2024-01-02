package blossom.project.config.core.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("tenant")
public class TenantScopedBean {
    // Bean的实现
}
