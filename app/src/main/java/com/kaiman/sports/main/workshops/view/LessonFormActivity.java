package com.kaiman.sports.main.workshops.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.contract.BaseView;
import com.core.util.AndroidUtils;
import com.core.util.PlaceholderRequestViewHelper;
import com.core.util.ViewUtils;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityLessonFormBinding;
import com.kaiman.sports.main.workshops.contract.LessonForm;
import com.kaiman.sports.main.workshops.model.LessonViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;

/**
 * Created by jhonnybarrios on 4/1/18
 */

public class LessonFormActivity extends BaseRequestActivity<ActivityLessonFormBinding> implements LessonForm.View, DatePickerDialog.OnDateSetListener {
    protected static final String KEY_IS_CREATING = "is_creating";
    protected static final String KEY_LESSON_ID = "lesson_id";
    protected static final String KEY_WORKSHOP_ID = "workshop_id";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String TIME_PATTERN = "HH:mm";
    private boolean isCreating = false;

    LessonForm.Presenter presenter;
    private boolean isOnEditMode;
    private boolean userCanEditLesson;

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return new PlaceholderRequestViewHelper(binder.root);
    }

    @Override
    protected void initView() {
        showUpButton(true);
        setupListeners();

        presenter = Injection.provideLessonFormPresenter();
        isCreating = getIntent().getExtras().getBoolean(KEY_IS_CREATING);
        presenter.setWorkshopId(getIntent().getExtras().getString(KEY_WORKSHOP_ID));
        setEditableMode(isCreating);
        if (isCreating) {
            setTitle(R.string.title_create_lesson);
        } else {
            presenter.setLessonId(getIntent().getExtras().getString(KEY_LESSON_ID));
            setTitle(R.string.title_lesson_detail);
        }
        presenter.initialize(this);
    }

    private void setupListeners() {
        binder.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.hideSoftKeyboard(v);
                presenter.attemptSave(getLessonTitle(),getStartDate(), getStartHour(), getEndHour(), getObjetive(), getContent());
            }
        });
        binder.inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isClickable()) {
                    showDatePickerDialog();
                }
            }
        });
        binder.inputStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isClickable()) {
                    showTimePickerDialog(binder.inputStartHour, getStartHour());
                }
            }
        });

        binder.inputEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isClickable()) {
                    showTimePickerDialog(binder.inputEndHour, getEndHour());
                }
            }
        });
    }

    private void showTimePickerDialog(final TextInputEditText inputEditText, String lastTime) {
        DateTime date;
        if (!lastTime.isEmpty()) {
            date = DateTime.parse(lastTime, new DateTimeFormatterBuilder().appendPattern(TIME_PATTERN).toFormatter());
        } else {
            date = new DateTime();
        }

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        DateTime time = new DateTime().withTime(hourOfDay, minute, 0, 0);
                        inputEditText.setText(time.toString(TIME_PATTERN));
                    }
                },
                date.getHourOfDay(),
                date.getMinuteOfHour(),
                true
        );
        tpd.enableSeconds(false);
        tpd.show(getFragmentManager(), null);
    }

    private void showDatePickerDialog() {
        DateTime date;
        if (!getStartDate().isEmpty()) {
           date = DateTime.parse(getStartDate(), new DateTimeFormatterBuilder().appendPattern(DATE_PATTERN).toFormatter());
           date.minusMonths(1);
        } else {
            date = new DateTime();
        }

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                LessonFormActivity.this,
                date.getYear(),
                date.getMonthOfYear()-1,
                date.getDayOfMonth()
        );
        Calendar now = Calendar.getInstance();
        dpd.setMinDate(now);
        dpd.show(getFragmentManager(), null);
    }

    private void setEditableMode(boolean editable) {
        isOnEditMode = editable;
        makeEditable(binder.inputContent, editable);
        makeEditable(binder.inputTitle, editable);
        makeEditable(binder.inputObjetive, editable);
        makeEditable(binder.inputDate, editable);
        makeEditable(binder.inputStartHour, editable);
        makeEditable(binder.inputEndHour, editable);
        makeFocusable(binder.inputDate, false);
        makeFocusable(binder.inputEndHour, false);
        makeFocusable(binder.inputStartHour, false);
        binder.inputTitle.requestFocus();
        invalidateOptionsMenu();
        if (!editable) {
            setTitle(R.string.title_lesson_detail);
        } else {
            setTitle(getString(!isCreating ? R.string.title_edit_lesson : R.string.title_create_lesson));
        }
        binder.buttonSave.setVisibility(editable ? View.VISIBLE: View.GONE);
    }

    private void makeFocusable(TextInputEditText editText, boolean able) {
        editText.setFocusable(able);
        editText.setFocusableInTouchMode(able);
    }

    private void makeEditable(TextInputEditText editText, boolean editable) {
        makeFocusable(editText, editable);
        editText.setClickable(editable);
        if (!editable) {
            ViewCompat.setBackgroundTintList(editText,ViewUtils.createTintList(Color.WHITE,Color.WHITE));
        } else {
            ViewCompat.setBackgroundTintList(editText,ViewUtils
                    .createTintList(getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimaryDark)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                setEditableMode(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getMenuId() {
        if (isCreating || isOnEditMode || !userCanEditLesson) {
            return R.menu.empty_menu;
        } else {
            return R.menu.edit_lesson;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lesson_form;
    }

    @Override
    public void setupLesson(LessonViewModel lesson) {
        binder.inputContent.setText(lesson.content);
        setDate(lesson.startDate);
        if (!lesson.endHour.trim().isEmpty()) {
            binder.inputEndHour.setText(lesson.endHour);
        }

        if (!lesson.startHour.trim().isEmpty()) {
            binder.inputStartHour.setText(lesson.startHour);
        }

        binder.inputTitle.setText(lesson.title);
        binder.inputObjetive.setText(lesson.objetive);
    }

    private void setDate(DateTime date) {

        binder.inputDate.setText(date.toString(DATE_PATTERN));
    }

    @Override
    public void clearErrorFromFields() {
        binder.inputLayoutContent.setError(null);
        binder.inputLayoutDate.setError(null);
        binder.inputLayoutEndHour.setError(null);
        binder.inputLayoutStartHour.setError(null);
        binder.inputLayoutTitle.setError(null);
        binder.inputLayoutObjetive.setError(null);
    }

    @Override
    public String getLessonTitle() {
        return binder.inputTitle.getText().toString().trim();
    }

    @Override
    public String getStartDate() {
        return binder.inputDate.getText().toString().trim();
    }

    @Override
    public String getStartHour() {
        return binder.inputStartHour.getText().toString().trim();
    }

    @Override
    public String getEndHour() {
        return binder.inputEndHour.getText().toString().trim();
    }

    @Override
    public String getObjetive() {
        return binder.inputObjetive.getText().toString().trim();
    }

    @Override
    public String getContent() {
        return binder.inputContent.getText().toString().trim();
    }

    @Override
    public void showSuccessCreationMessage() {
        showMessage(R.string.great, getString(isCreating ? R.string.successfull_lesson_creation : R.string.successfull_lesson_edition));
    }

    @Override
    public void showTitleEmptyError() {
        binder.inputLayoutTitle.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showStartDateEmptyError() {
        binder.inputLayoutDate.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showStartHourEmptyError() {
        binder.inputLayoutStartHour.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showEndHourEmptyError() {
        binder.inputLayoutEndHour.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showObjetiveEmptyError() {
        binder.inputLayoutObjetive.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showContentEmptyError() {
        binder.inputLayoutContent.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void setUserCanEditLesson(boolean canEdit) {
        this.userCanEditLesson = canEdit;
    }

    @Override
    public boolean isOnEditMode() {
        return isOnEditMode && !isCreating;
    }

    @Override
    public void onBackPressed() {
        if (isOnEditMode && !isCreating) {
            setEditableMode(false);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        DateTime newDate = new DateTime(year, monthOfYear+1, dayOfMonth, 0, 0);
        setDate(newDate);
    }
}