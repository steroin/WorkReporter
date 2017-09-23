package pl.workreporter.web.controllers.employee;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.entities.user.UserDaoWrapper;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;
import pl.workreporter.web.service.mail.MailNotifier;
import pl.workreporter.web.service.password.generator.PasswordGenerator;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Sergiusz on 19.08.2017.
 */
@RestController
public class EmployeeRestController {
    @Autowired
    private UserDaoWrapper userDaoWrapper;
    @Autowired
    private PasswordGenerator passwordGenerator;
    @Autowired
    private MailNotifier notifier;

    @RequestMapping(value = "/solution/employees", params = "id",  method = GET)
    @JsonView(JsonDataView.SolutionManager.class)
    public @ResponseBody
    RestResponse<List<User>> getAllEmployees(@RequestParam("id") long solutionId) {
        return userDaoWrapper.getAllUsersInSolution(solutionId);
    }

    @RequestMapping(value = "/solution/employees", params = "teamid", method = GET)
    @JsonView(JsonDataView.SolutionManager.class)
    public @ResponseBody
    RestResponse<List<User>> getAllEmployeesInTeam(@RequestParam("teamid") long teamId) {
        return userDaoWrapper.getAllUsersInTeam(teamId);
    }

    @RequestMapping(value = "/solution/employees/{userid}", method = DELETE)
    public RestResponse<Void> removeEmployee(@PathVariable("userid") long employeeId) {
        return userDaoWrapper.removeUser(employeeId);
    }

    @RequestMapping(value = "/solution/employees", method = DELETE)
    public RestResponse<Void> removeSelectedEmployees(@RequestParam("employees") List<Long> employees) {
        return userDaoWrapper.removeUsers(employees);
    }

    @RequestMapping(value = "/solution/employees/{id}", method = PATCH)
    @JsonView(JsonDataView.SolutionManager.class)
    public RestResponse<User> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Map<String, String> employee) {
        return userDaoWrapper.updateUser(employeeId, employee);
    }

    @RequestMapping(value="/solution/employees", method = POST)
    @JsonView(JsonDataView.SolutionManager.class)
    public RestResponse<User> addEmployee(@RequestBody Map<String, String> employee) {
        String password = passwordGenerator.generate(12);
        Long teamId = employee.get("teamid").isEmpty() ? null : Long.parseLong(employee.get("teamid"));
        Long positionId = employee.get("positionid").isEmpty() ? null : Long.parseLong(employee.get("positionid"));
        String birthday = employee.get("birthday").isEmpty() ? null : employee.get("birthday");
        String phone = employee.get("phone").isEmpty() ? null : employee.get("phone");
        notifier.sendInitialMessage(employee.get("login"), password, employee.get("email"));
        return userDaoWrapper.addUser(Long.parseLong(employee.get("solutionid")), teamId,
                positionId, Double.parseDouble(employee.get("workingtime")),
                employee.get("firstname"), employee.get("lastname"), birthday, phone,
                employee.get("login"), password, employee.get("email"));
    }
}
